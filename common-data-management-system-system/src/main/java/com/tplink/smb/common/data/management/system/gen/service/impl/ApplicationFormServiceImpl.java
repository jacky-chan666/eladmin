/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.impl;

import static com.tplink.smb.common.data.management.system.gen.domain.ApplicationForm.APPLICATION_DATATYPE_DEVICEINFO;
import static com.tplink.smb.common.data.management.system.gen.domain.ApplicationForm.APPLICATION_DATATYPE_GATEWAYINFO;
import static com.tplink.smb.common.data.management.system.gen.domain.ApplicationForm.APPLICATION_TYPE_ADD;
import static com.tplink.smb.common.data.management.system.gen.domain.ApplicationForm.APPLICATION_TYPE_EDIT;
import static com.tplink.smb.common.data.management.system.gen.domain.ApplicationForm.APPLICATION_TYPE_OFFLINE;
import static com.tplink.smb.common.data.management.system.gen.domain.ApplicationForm.APPLICATION_TYPE_ONLINE;
import static com.tplink.smb.common.data.management.system.gen.domain.ApprovalRecord.STATUS_APPROVED;
import static com.tplink.smb.common.data.management.system.gen.domain.ApprovalRecord.STATUS_PEDING;
import static com.tplink.smb.common.data.management.system.gen.domain.ApprovalRecord.STATUS_REJECTED;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tplink.smb.common.data.management.system.exception.EntityExistException;
import com.tplink.smb.common.data.management.system.gen.domain.*;
import com.tplink.smb.common.data.management.system.gen.domain.vo.ApplicationFormVo;
import com.tplink.smb.common.data.management.system.gen.repository.ApplicationFormRepository;
import com.tplink.smb.common.data.management.system.gen.repository.DeviceInfoRepository;
import com.tplink.smb.common.data.management.system.gen.service.ApplicationFormService;
import com.tplink.smb.common.data.management.system.gen.service.DataInfoService;
import com.tplink.smb.common.data.management.system.gen.service.DeviceInfoService;
import com.tplink.smb.common.data.management.system.gen.service.GatewayInfoService;
import com.tplink.smb.common.data.management.system.gen.service.dto.ApplicationFormDto;
import com.tplink.smb.common.data.management.system.gen.service.dto.ApplicationFormQueryCriteria;
import com.tplink.smb.common.data.management.system.gen.service.dto.ApprovalRecordDto;
import com.tplink.smb.common.data.management.system.gen.service.dto.PendingApprovalDto;
import com.tplink.smb.common.data.management.system.gen.service.mapstruct.ApplicationFormMapper;
import com.tplink.smb.common.data.management.system.service.S3StorageService;
import com.tplink.smb.common.data.management.system.utils.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Service
@RequiredArgsConstructor
public class ApplicationFormServiceImpl implements ApplicationFormService {

  private final ApplicationFormRepository applicationFormRepository;
  private final ApplicationFormMapper applicationFormMapper;
  private final DeviceInfoRepository deviceInfoRepository;
  private final DeviceInfoService deviceInfoService;
  private final GatewayInfoService gatewayInfoService;
  private final S3StorageService s3StorageService;

  /** 将 VO 中的数据复制到 JPA Entity */
  private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson

  // 添加依赖注入
  @Autowired private PlatformTransactionManager transactionManager;

  // TODO 代码注释和代码规范，文件注释都要修改
  // 修改 ApplicationFormServiceImpl.java
  @Override
  public PageResult<ApplicationFormDto> queryAll(
      ApplicationFormQueryCriteria criteria, Pageable pageable) {
    Page<ApplicationForm> page =
        applicationFormRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder),
            pageable);

    // 转换为 DTO 并添加审批信息
    Page<ApplicationFormDto> dtoPage =
        page.map(
            applicationForm -> {
              ApplicationFormDto dto = applicationFormMapper.toDto(applicationForm);
              // 添加审批信息
              enrichApprovalInfo(dto, applicationForm.getId());
              // 添加图片预签名URL信息
              enrichImageUrls(dto, applicationForm);
              return dto;
            });

    return PageUtil.toPage(dtoPage);
  }

  @Override
  public List<ApplicationFormDto> queryAll(ApplicationFormQueryCriteria criteria) {
    List<ApplicationForm> forms =
        applicationFormRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder));

    // 转换为 DTO 并添加审批信息
    return forms.stream()
        .map(
            form -> {
              ApplicationFormDto dto = applicationFormMapper.toDto(form);
              // 添加审批信息
              enrichApprovalInfo(dto, form.getId());
              // 添加图片预签名URL信息到dataDetails中
              enrichImageUrls(dto, form);
              return dto;
            })
        .collect(Collectors.toList());
  }

  /**
   * 丰富申请单的审批信息
   *
   * @param dto 申请单 DTO
   * @param applicationFormId 申请单 ID
   */
  private void enrichApprovalInfo(ApplicationFormDto dto, Integer applicationFormId) {
    ApplicationForm form = applicationFormRepository.findById(applicationFormId).orElse(null);
    if (form != null && form.getApprovalRecords() != null) {
      dto.setApprovalRecords(form.getApprovalRecords()); // 直接使用 approvalRecords 字段

      // 计算每个审批人的最新审批状态和意见
      enrichApproverStatusAndComments(dto, form);
    } else {
      dto.setApprovalRecords("[]");
    }
  }

  /**
   * 丰富每个审批人的状态和意见信息
   *
   * @param dto 申请单 DTO
   * @param form 申请单实体
   */
  private void enrichApproverStatusAndComments(ApplicationFormDto dto, ApplicationForm form) {
    List<Map<String, Object>> records = getApprovalRecordsFromForm(form);

    // 获取最新轮次
    int maxRound = form.getRound();

    // 过滤出最新轮次的记录
    List<Map<String, Object>> latestRoundRecords =
        records.stream()
            .filter(r -> (Integer) r.getOrDefault("round", 0) == maxRound)
            .collect(Collectors.toList());

    // 为每个审批人设置状态和意见
    for (Map<String, Object> record : latestRoundRecords) {
      String approverUserName = (String) record.get("approverUserName");
      Integer approvalStatus = (Integer) record.get("approvalStatus");
      String comment = (String) record.get("comment");

      // 根据审批人用户名匹配并设置状态和意见
      if (Objects.equals(approverUserName, dto.getApprover1())) {
        dto.setApprover1Status(approvalStatus);
        dto.setApprover1Comment(comment);
      } else if (Objects.equals(approverUserName, dto.getApprover2())) {
        dto.setApprover2Status(approvalStatus);
        dto.setApprover2Comment(comment);
      } else if (Objects.equals(approverUserName, dto.getApprover3())) {
        dto.setApprover3Status(approvalStatus);
        dto.setApprover3Comment(comment);
      } else if (Objects.equals(approverUserName, dto.getApprover4())) {
        dto.setApprover4Status(approvalStatus);
        dto.setApprover4Comment(comment);
      } else if (Objects.equals(approverUserName, dto.getApprover5())) {
        dto.setApprover5Status(approvalStatus);
        dto.setApprover5Comment(comment);
      } else if (Objects.equals(approverUserName, dto.getApprover6())) {
        dto.setApprover6Status(approvalStatus);
        dto.setApprover6Comment(comment);
      }
    }
  }

  /**
   * 丰富申请单的图片URL信息，直接在dataDetails中添加预签名URL
   *
   * @param dto 申请单 DTO
   * @param applicationForm 申请单实体
   */
  private void enrichImageUrls(ApplicationFormDto dto, ApplicationForm applicationForm) {
    try {
      // 检查是否是设备信息类型的申请单
      if (APPLICATION_DATATYPE_DEVICEINFO != applicationForm.getApplicationDataType()) {
        return;
      }

      // 解析 dataDetails JSON 字符串
      String dataDetails = applicationForm.getDataDetails();
      if (dataDetails == null || dataDetails.isEmpty()) {
        return;
      }

      // 使用 ObjectMapper 解析 JSON
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> dataMap = objectMapper.readValue(dataDetails, Map.class);

      // 提取图片路径并生成预签名URL
      boolean updated = false;

      // 提取小图路径并生成预签名URL
      String smallImgPath = (String) dataMap.get("smallImgBucketPathForWeb");
      if (StringUtils.isNotBlank(smallImgPath)) {
        try {
          String smallImgUrl = s3StorageService.generatePresignedUrl(smallImgPath, 3600); // 1小时有效期
          dataMap.put("smallImgUrl", smallImgUrl);
          updated = true;
        } catch (Exception e) {
          // 记录日志但不中断流程
        }
      }

      // 提取大图路径并生成预签名URL
      String bigImgPath = (String) dataMap.get("bigImgBucketPathForWeb");
      if (StringUtils.isNotBlank(bigImgPath)) {
        try {
          String bigImgUrl = s3StorageService.generatePresignedUrl(bigImgPath, 3600);
          dataMap.put("bigImgUrl", bigImgUrl);
          updated = true;
        } catch (Exception e) {
          // 记录日志但不中断流程
        }
      }

      // 提取热力图路径并生成预签名URL
      String heatmapImgPath = (String) dataMap.get("heatmapImgBucketPathForWeb");
      if (StringUtils.isNotBlank(heatmapImgPath)) {
        try {
          String heatmapImgUrl = s3StorageService.generatePresignedUrl(heatmapImgPath, 3600);
          dataMap.put("heatmapImgUrl", heatmapImgUrl);
          updated = true;
        } catch (Exception e) {
          // 记录日志但不中断流程
        }
      }

      // 提取高清图路径并生成预签名URL
      String hdpiImgPath = (String) dataMap.get("hdpiImgBucketPathForApp");
      if (StringUtils.isNotBlank(hdpiImgPath)) {
        try {
          String hdpiImgUrl = s3StorageService.generatePresignedUrl(hdpiImgPath, 3600);
          dataMap.put("hdpiImgUrl", hdpiImgUrl);
          updated = true;
        } catch (Exception e) {
          // 记录日志但不中断流程
        }
      }

      // 如果有更新，则重新序列化 dataDetails
      if (updated) {
        String updatedDataDetails = objectMapper.writeValueAsString(dataMap);
        dto.setDataDetails(updatedDataDetails);
      }

    } catch (Exception e) {
      // 解析失败时记录日志但不中断流程
    }
  }

  @Override
  @Transactional
  public ApplicationFormDto findById(Integer id) {
    ApplicationForm applicationForm =
        applicationFormRepository.findById(id).orElseGet(ApplicationForm::new);
    ValidationUtil.isNull(applicationForm.getId(), "ApplicationForm", "id", id);

    ApplicationFormDto dto = applicationFormMapper.toDto(applicationForm);
    // 添加审批信息
    enrichApprovalInfo(dto, id);

    return dto;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void create(ApplicationForm resources) {
    if (applicationFormRepository.findByUuid(resources.getUuid()) != null) {
      throw new EntityExistException(ApplicationForm.class, "uuid", resources.getUuid());
    }
    if (resources.getApplicationDataId() != null
        && applicationFormRepository.findByApplicationDataId(resources.getApplicationDataId())
            != null) {
      throw new EntityExistException(
          ApplicationForm.class,
          "application_data_id",
          resources.getApplicationDataId().toString());
    }
    applicationFormRepository.save(resources);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(ApplicationForm resources) {
    ApplicationForm applicationForm =
        applicationFormRepository.findById(resources.getId()).orElseGet(ApplicationForm::new);
    ValidationUtil.isNull(applicationForm.getId(), "ApplicationForm", "id", resources.getId());
    ApplicationForm applicationForm1 = null;
    applicationForm1 = applicationFormRepository.findByUuid(resources.getUuid());
    if (applicationForm1 != null && !applicationForm1.getId().equals(applicationForm.getId())) {
      throw new EntityExistException(ApplicationForm.class, "uuid", resources.getUuid());
    }
    applicationForm1 =
        applicationFormRepository.findByApplicationDataId(resources.getApplicationDataId());
    if (resources.getApplicationDataId() != null
        && applicationForm1 != null
        && !applicationForm1.getId().equals(applicationForm.getId())) {
      throw new EntityExistException(
          ApplicationForm.class,
          "application_data_id",
          resources.getApplicationDataId().toString());
    }
    applicationForm.copy(resources);
    applicationFormRepository.save(applicationForm);
  }

  @Override
  public void deleteAll(Integer[] ids) {
    for (Integer id : ids) {
      applicationFormRepository.deleteById(id);
    }
  }

  // ApplicationFormServiceImpl.java
  @Override
  public void download(List<ApplicationFormDto> all, HttpServletResponse response)
      throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (ApplicationFormDto applicationForm : all) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("申请单UUID", applicationForm.getUuid());
      map.put("申请人姓名", applicationForm.getApplicantUserName());
      map.put("所属部门", applicationForm.getDepartment());
      map.put("申请数据id", applicationForm.getApplicationDataId());
      map.put("申请单类型：新增，修改，上线，下线", applicationForm.getApplicationType());
      map.put("申请单数据类型：1-deviceInfo，2-gatewayInfo", applicationForm.getApplicationDataType());
      map.put("申请单标题", applicationForm.getApplicationTitle());
      map.put("申请理由", applicationForm.getApplicationReason());
      map.put("申请状态", applicationForm.getStatus());
      map.put("审批人1", applicationForm.getApprover1());
      map.put("审批人1层级", applicationForm.getApprover1Level());
      map.put("审批人2", applicationForm.getApprover2());
      map.put("审批人2层级", applicationForm.getApprover2Level());
      map.put("审批人3", applicationForm.getApprover3());
      map.put("审批人3层级", applicationForm.getApprover3Level());
      map.put("审批人4", applicationForm.getApprover4());
      map.put("审批人4层级", applicationForm.getApprover4Level());
      map.put("审批人5", applicationForm.getApprover5());
      map.put("审批人5层级", applicationForm.getApprover5Level());
      map.put("审批人6", applicationForm.getApprover6());
      map.put("审批人6层级", applicationForm.getApprover6Level());
      map.put("当前审核人列表（JSON格式存储）", applicationForm.getCurrentApprovers());
      map.put(" createdAt", applicationForm.getCreatedAt());
      map.put(" updatedAt", applicationForm.getUpdatedAt());
      list.add(map);
    }
    FileUtil.downloadExcel(list, response);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void submitApplication(ApplicationFormVo vo) {
    Timestamp now = new Timestamp(System.currentTimeMillis());

    // 查询是否已存在该申请单（根据 id 判断）
    ApplicationForm form;
    boolean isNew = vo.getId() == null;

    if (isNew) {
      form = new ApplicationForm();
      form.setStatus(ApplicationForm.STATUS_SUBMITTED); // 已提交（修改为新状态）
      form.setUuid(generateApplicationNo());
      form.setRound(1); // 第一次提交，轮次为1
      form.setCreatedAt(now);
      form.setUpdatedAt(now);
    } else {
      form =
          applicationFormRepository
              .findById(vo.getId())
              .orElseThrow(() -> new RuntimeException("申请单不存在"));
      // 更新模式：复制新数据并设置为已提交状态
      form.setRound(form.getRound() + 1); // 轮次+1 草稿的轮次为0 只要提交就+1
      form.setUpdatedAt(now);
      form.setStatus(ApplicationForm.STATUS_SUBMITTED); // 已提交（修改为新状态）
    }

    // === 手动映射 VO 字段到 Entity ===
    mapVoToEntity(form, vo);

    // 保存申请单
    applicationFormRepository.save(form);
  }

  /**
   * 生成申请单编号 格式: APP + 年月日 + 6位序列号 (例如: APP20250918000001)
   *
   * @return 申请单编号
   */
  private String generateApplicationNo() {
    // 获取当前日期
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String dateStr = sdf.format(new Date());

    // 生成6位随机数或序列号
    // 这里使用随机数，实际项目中可以使用数据库序列或其他方式保证唯一性
    Random random = new Random();
    int randomNumber = 100000 + random.nextInt(900000); // 生成100000-999999之间的数字

    return "APP" + dateStr + randomNumber;
  }

  private void mapVoToEntity(ApplicationForm entity, ApplicationFormVo vo) {
    // 基础字段（标题、理由等）
    entity.setApplicationTitle(vo.getApplicationTitle());
    entity.setApplicationReason(vo.getApplicationReason());
    entity.setApplicationType(vo.getApplicationType());
    entity.setApplicationDataType(vo.getApplicationDataType());
    entity.setApplicationDataId(vo.getApplicationDataId());

    // 设置审批人信息
    entity.setApprover1(vo.getApprover1());
    entity.setApprover1Level(vo.getApprover1Level());
    entity.setApprover2(vo.getApprover2());
    entity.setApprover2Level(vo.getApprover2Level());
    entity.setApprover3(vo.getApprover3());
    entity.setApprover3Level(vo.getApprover3Level());
    entity.setApprover4(vo.getApprover4());
    entity.setApprover4Level(vo.getApprover4Level());
    entity.setApprover5(vo.getApprover5());
    entity.setApprover5Level(vo.getApprover5Level());
    entity.setApprover6(vo.getApprover6());
    entity.setApprover6Level(vo.getApprover6Level());

    // 申请人信息
    entity.setApplicantUserName(vo.getApplicantUserName());

    // 根据数据类型存储不同的详细信息
    entity.setDataDetails(vo.getDataDetails());
  }

  // 修改 ApplicationFormServiceImpl.java 中的 withdrawApplication 方法
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void withdrawApplication(Integer applicationFormId, String applicantName) {
    ApplicationForm form =
        applicationFormRepository
            .findById(applicationFormId)
            .orElseThrow(() -> new RuntimeException("申请单不存在"));

    // 检查申请人是否匹配
    if (!form.getApplicantUserName().equals(applicantName)) {
      throw new RuntimeException("只有申请人才能撤回申请单");
    }

    // 检查当前状态是否允许撤回（已提交、固件校验失败、同步失败状态下才能撤回）
    if (!form.getStatus().equals(ApplicationForm.STATUS_SUBMITTED)
        && !form.getStatus().equals(ApplicationForm.STATUS_FIRMWARE_FAILED)
        && !form.getStatus().equals(ApplicationForm.STATUS_SYNC_FAILED)) {
      throw new RuntimeException("当前状态不允许撤回申请单");
    }

    // 更新状态为已撤回
    form.setStatus(ApplicationForm.STATUS_WITHDRAWN);
    form.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    // form.setApprovalRecords("[]"); // 清空审批记录
    applicationFormRepository.save(form);
  }

  // 修改 ApplicationFormServiceImpl.java 中的 getPendingApprovals 方法（非分页版本）
  @Override
  public List<PendingApprovalDto> getPendingApprovals(String approverUserName) {
    List<ApplicationForm> forms = applicationFormRepository.findAll();

    List<PendingApprovalDto> result = new ArrayList<>();
    for (ApplicationForm form : forms) {
      // 只处理已提交或待审批状态的申请单
      if (form.getStatus().equals(ApplicationForm.STATUS_SUBMITTED)
          || form.getStatus().equals(ApplicationForm.STATUS_PENDING)) {

        // 检查该用户是否是当前需要审批的人
        if (isUserCurrentApprover(form, approverUserName)) {
          ApplicationFormDto formDto = applicationFormMapper.toDto(form);
          enrichApprovalInfo(formDto, form.getId());
          PendingApprovalDto dto = new PendingApprovalDto();
          dto.setApplicationForm(formDto);
          // 直接使用表中的轮次值
          dto.setRound(form.getRound());
          // 设置步骤为当前需要审批的最高层级
          dto.setStepOrder(getCurrentApprovalLevel(form, approverUserName));
          result.add(dto);
        }
      }
    }

    return result;
  }

  // 修改 ApplicationFormServiceImpl.java 中的 getPendingApprovals 方法（分页版本）
  @Override
  @Transactional(readOnly = true)
  public PageResult<PendingApprovalDto> getPendingApprovals(
      ApplicationFormQueryCriteria criteria, Pageable pageable) {
    // 先查询所有申请单
    List<ApplicationForm> allForms = applicationFormRepository.findAll();

    // 过滤出包含当前审批人的申请单，减少不必要的数据库查询
    List<ApplicationForm> filteredForms =
        allForms.stream()
            .filter(
                form ->
                    form.getStatus().equals(ApplicationForm.STATUS_SUBMITTED)
                        || form.getStatus().equals(ApplicationForm.STATUS_PENDING))
            .filter(form -> containsApprover(form, criteria.getApproverUserName())) // 先粗筛
            .filter(form -> isUserCurrentApprover(form, criteria.getApproverUserName())) // 再细筛
            .collect(Collectors.toList());

    // 处理筛选后的申请单
    List<PendingApprovalDto> result =
        filteredForms.stream()
            .map(
                form -> {
                  ApplicationFormDto formDto = applicationFormMapper.toDto(form);
                  enrichApprovalInfo(formDto, form.getId());
                  PendingApprovalDto dto = new PendingApprovalDto();
                  dto.setApplicationForm(formDto);
                  // 直接使用表中的轮次值
                  dto.setRound(form.getRound());
                  dto.setStepOrder(getCurrentApprovalLevel(form, criteria.getApproverUserName()));
                  return dto;
                })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    // 手动分页
    int total = result.size();
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), total);

    List<PendingApprovalDto> pagedContent = result.subList(start, end);

    // 构造 PageResult（使用项目中定义的字段）
    PageResult<PendingApprovalDto> pageResult = new PageResult<>();
    pageResult.setContent(pagedContent);
    pageResult.setTotalElements(total);

    return pageResult;
  }

  /** 粗筛：判断申请单是否包含指定审批人 */
  private boolean containsApprover(ApplicationForm form, String approverUserName) {
    return Objects.equals(form.getApprover1(), approverUserName)
        || Objects.equals(form.getApprover2(), approverUserName)
        || Objects.equals(form.getApprover3(), approverUserName)
        || Objects.equals(form.getApprover4(), approverUserName)
        || Objects.equals(form.getApprover5(), approverUserName)
        || Objects.equals(form.getApprover6(), approverUserName);
  }

  // ApplicationFormServiceImpl.java
  @Override
  @Transactional(readOnly = true)
  public PageResult<ApplicationFormDto> getApprovedApplications(
      ApplicationFormQueryCriteria criteria, Pageable pageable) {
    // 先查询所有申请单
    List<ApplicationForm> allForms = applicationFormRepository.findAll();

    // 过滤出当前用户已审批的申请单
    List<ApplicationForm> filteredForms =
        allForms.stream()
            .filter(form -> containsApprover(form, criteria.getApproverUserName()))
            .filter(form -> isUserApprovedApplication(form, criteria.getApproverUserName()))
            .filter(form -> matchesCriteria(form, criteria))
            .collect(Collectors.toList());

    // 转换为 ApplicationFormDto 列表
    List<ApplicationFormDto> result =
        filteredForms.stream()
            .map(
                form -> {
                  ApplicationFormDto dto = applicationFormMapper.toDto(form);
                  enrichApprovalInfo(dto, form.getId());
                  return dto;
                })
            .collect(Collectors.toList());

    // 手动分页
    int total = result.size();
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), total);

    List<ApplicationFormDto> pagedContent = new ArrayList<>();
    if (start < end) {
      pagedContent = result.subList(start, end);
    }

    // 构造 PageResult（使用项目中定义的字段）
    PageResult<ApplicationFormDto> pageResult = new PageResult<>();
    pageResult.setContent(pagedContent);
    pageResult.setTotalElements(total);

    return pageResult;
  }

  /** 判断用户是否已审批该申请单 */
  private boolean isUserApprovedApplication(ApplicationForm form, String approverUserName) {
    List<Map<String, Object>> records = getApprovalRecordsFromForm(form);
    return records.stream()
        .anyMatch(
            r -> {
              Object approverUserNameObj = r.get("approverUserName");
              Object approvalStatusObj = r.get("approvalStatus");
              return approverUserNameObj != null
                  && approvalStatusObj != null
                  && approverUserNameObj.equals(approverUserName)
                  && ((Integer) approvalStatusObj == ApprovalRecord.STATUS_APPROVED
                      || (Integer) approvalStatusObj == ApprovalRecord.STATUS_REJECTED);
            });
  }

  /** 判断申请单是否匹配查询条件 */
  private boolean matchesCriteria(ApplicationForm form, ApplicationFormQueryCriteria criteria) {
    // 根据申请单UUID过滤
    if (criteria.getUuid() != null && !criteria.getUuid().isEmpty()) {
      if (form.getUuid() == null || !form.getUuid().contains(criteria.getUuid())) {
        return false;
      }
    }

    // 根据申请人姓名过滤
    if (criteria.getApplicantUserName() != null && !criteria.getApplicantUserName().isEmpty()) {
      if (form.getApplicantUserName() == null
          || !form.getApplicantUserName().contains(criteria.getApplicantUserName())) {
        return false;
      }
    }

    // 根据申请单类型过滤
    if (criteria.getApplicationType() != null) {
      if (form.getApplicationType() == null
          || !form.getApplicationType().equals(criteria.getApplicationType())) {
        return false;
      }
    }

    // 根据申请单数据类型过滤
    if (criteria.getApplicationDataType() != null) {
      if (form.getApplicationDataType() == null
          || !form.getApplicationDataType().equals(criteria.getApplicationDataType())) {
        return false;
      }
    }

    // 根据申请单标题过滤
    if (criteria.getApplicationTitle() != null && !criteria.getApplicationTitle().isEmpty()) {
      return form.getApplicationTitle() != null
          && form.getApplicationTitle().contains(criteria.getApplicationTitle());
    }

    return true;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void approveApplication(
      Integer applicationFormId, String approverUserName, Integer approvalStatus, String comment) {
    Optional<ApplicationForm> formOpt = applicationFormRepository.findById(applicationFormId);
    if (!formOpt.isPresent()) {
      throw new RuntimeException("申请单不存在");
    }

    ApplicationForm form = formOpt.get();
    Timestamp now = new Timestamp(System.currentTimeMillis());

    // 检查申请单状态是否为已提交或待审批
    if (!form.getStatus().equals(ApplicationForm.STATUS_SUBMITTED)
        && !form.getStatus().equals(ApplicationForm.STATUS_PENDING)) {
      throw new RuntimeException("申请单状态不正确，无法审批");
    }

    // 检查该用户是否是当前需要审批的人
    if (!isUserCurrentApprover(form, approverUserName)) {
      throw new RuntimeException("当前用户不是需要审批的人");
    }

    // 获取当前审批轮次（直接从实体中获取）
    Integer currentRound = form.getRound();

    // 如果申请单状态是已提交，将其改为待审批
    if (form.getStatus().equals(ApplicationForm.STATUS_SUBMITTED)) {
      form.setStatus(ApplicationForm.STATUS_PENDING);
    }

    // 创建审批记录
    Map<String, Object> record = new LinkedHashMap<>();
    record.put("id", UUID.randomUUID().toString());
    record.put("applicationFormId", applicationFormId);
    record.put("round", currentRound); // 使用表中的轮次值
    record.put("approverLevel", getCurrentApprovalLevel(form, approverUserName));
    record.put("approverRole", getApproverRole(form, approverUserName));
    record.put("approverUserName", approverUserName);
    record.put("approvalStatus", approvalStatus);
    record.put("comment", comment);
    record.put("approvedAt", now);

    // 更新审批记录到 JSON 字段
    updateApprovalRecords(form, record);

    if (Objects.equals(approvalStatus, ApprovalRecord.STATUS_REJECTED)) {
      // 审批被驳回，更新申请单状态
      form.setStatus(ApplicationForm.STATUS_REJECTED);
    } else {
      // 审批通过，检查当前层级是否全部完成
      if (isCurrentLevelCompleted(form, currentRound)) {
        // 检查是否还有更高层级需要审批
        if (hasNextLevel(form, currentRound)) {
          // 还有更高层级，保持待审批状态
        } else {
          // 所有层级都完成，整个审批流程结束
          form.setStatus(ApplicationForm.STATUS_APPROVED);
        }
      }
    }

    applicationFormRepository.save(form);

    // 如果审批通过且所有层级完成，处理后续流程
    if (form.getStatus().equals(ApplicationForm.STATUS_APPROVED)) {
      // 处理设备信息申请，信息存储入本地
      ApplicationPostProcess(applicationFormId);

      // 启动新线程执行自动流程，不使用事务
      new Thread(
              () -> {
                try {
                  startAutoProcess(applicationFormId);
                } catch (Exception e) {
                  // 记录日志，不影响主流程
                }
              })
          .start();
    }
  }

  // 更新审批记录
  private void updateApprovalRecords(ApplicationForm form, Map<String, Object> record) {
    List<Map<String, Object>> records = new ArrayList<>();

    // 如果已有记录，先解析出来
    if (form.getApprovalRecords() != null && !form.getApprovalRecords().isEmpty()) {
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        records =
            objectMapper.readValue(
                form.getApprovalRecords(), new TypeReference<List<Map<String, Object>>>() {});
      } catch (Exception e) {
        // 解析失败则使用空列表
      }
    }

    // 添加新记录
    records.add(record);

    // 序列化回 JSON
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      form.setApprovalRecords(objectMapper.writeValueAsString(records));
    } catch (Exception e) {
      // 序列化失败则设为空数组
      // form.setApprovalRecords("[]");
    }
  }

  // 从表单中获取审批记录
  private List<Map<String, Object>> getApprovalRecordsFromForm(ApplicationForm form) {
    if (form.getApprovalRecords() == null || form.getApprovalRecords().isEmpty()) {
      return new ArrayList<>();
    }

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(
          form.getApprovalRecords(), new TypeReference<List<Map<String, Object>>>() {});
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /** 判断用户是否是当前需要审批的人 */
  private boolean isUserCurrentApprover(ApplicationForm form, String approverUserName) {
    // 获取当前用户在审批流程中的层级
    int currentUserLevel = getCurrentApprovalLevel(form, approverUserName);

    // 获取所有审批记录
    List<Map<String, Object>> records = getApprovalRecordsFromForm(form);

    // 检查当前用户是否已经在该层级审批过（无论通过还是驳回）
    boolean userAlreadyApprovedInLevel =
        records.stream()
            .filter(
                r -> {
                  Object approverLevel = r.get("approverLevel");
                  Object approverUserNameObj = r.get("approverUserName");
                  return approverLevel != null
                      && approverUserNameObj != null
                      && (Integer) approverLevel == currentUserLevel
                      && approverUserNameObj.equals(approverUserName);
                })
            .findFirst()
            .isPresent();

    // 如果用户已在该层级审批过，则不是当前需要审批的人
    if (userAlreadyApprovedInLevel) {
      return false;
    }

    // 确定当前应该审批的层级
    // 查找最小的未完成审批的层级
    int currentExpectedLevel = determineCurrentApprovalLevel(form, records);

    // 检查当前用户是否在应该审批的层级上
    if (currentUserLevel != currentExpectedLevel) {
      return false;
    }

    // 获取当前层级的所有审批人
    List<String> currentLevelApprovers = getCurrentLevelApprovers(form, currentUserLevel);

    // 检查当前用户是否是该层级的审批人
    return currentLevelApprovers.contains(approverUserName);
  }

  /**
   * 确定当前应该审批的层级 规则：从层级1开始，检查每个层级是否所有审批人都已完成审批 如果某一层级还有未审批的审批人，则该层级为当前审批层级 如果某一层级所有审批人都已审批，则检查下一层级
   */
  private int determineCurrentApprovalLevel(
      ApplicationForm form, List<Map<String, Object>> records) {
    // 获取所有存在的层级
    Set<Integer> allLevels = new HashSet<>();
    if (form.getApprover1Level() != null) allLevels.add(form.getApprover1Level());
    if (form.getApprover2Level() != null) allLevels.add(form.getApprover2Level());
    if (form.getApprover3Level() != null) allLevels.add(form.getApprover3Level());
    if (form.getApprover4Level() != null) allLevels.add(form.getApprover4Level());
    if (form.getApprover5Level() != null) allLevels.add(form.getApprover5Level());
    if (form.getApprover6Level() != null) allLevels.add(form.getApprover6Level());

    if (allLevels.isEmpty()) {
      return 0;
    }

    // 按层级排序
    List<Integer> sortedLevels = new ArrayList<>(allLevels);
    Collections.sort(sortedLevels);

    // 检查每个层级是否已完成审批
    for (Integer level : sortedLevels) {
      List<String> levelApprovers = getCurrentLevelApprovers(form, level);
      if (levelApprovers.isEmpty()) {
        continue;
      }

      // 统计该层级已审批通过的人数
      long approvedCount =
          records.stream()
              .filter(
                  r -> {
                    Object approverLevel = r.get("approverLevel");
                    Object approvalStatus = r.get("approvalStatus");
                    Object approverUserNameObj = r.get("approverUserName");
                    return approverLevel != null
                        && approvalStatus != null
                        && approverUserNameObj != null
                        && approverLevel == level
                        && (Integer) approvalStatus == ApprovalRecord.STATUS_APPROVED
                        && levelApprovers.contains(approverUserNameObj);
                  })
              .count();

      // 如果该层级还有未审批的人，则当前审批层级就是这一层
      if (approvedCount < levelApprovers.size()) {
        return level;
      }
    }

    // 如果所有层级都已完成审批，返回最大层级+1（理论上不会发生）
    return sortedLevels.get(sortedLevels.size() - 1) + 1;
  }

  /** 获取指定层级的所有审批人 */
  private List<String> getCurrentLevelApprovers(ApplicationForm form, int level) {
    List<String> approvers = new ArrayList<>();

    if (form.getApprover1Level() != null
        && form.getApprover1Level() == level
        && form.getApprover1() != null
        && !form.getApprover1().isEmpty()) {
      approvers.add(form.getApprover1());
    }

    if (form.getApprover2Level() != null
        && form.getApprover2Level() == level
        && form.getApprover2() != null
        && !form.getApprover2().isEmpty()) {
      approvers.add(form.getApprover2());
    }

    if (form.getApprover3Level() != null
        && form.getApprover3Level() == level
        && form.getApprover3() != null
        && !form.getApprover3().isEmpty()) {
      approvers.add(form.getApprover3());
    }

    if (form.getApprover4Level() != null
        && form.getApprover4Level() == level
        && form.getApprover4() != null
        && !form.getApprover4().isEmpty()) {
      approvers.add(form.getApprover4());
    }

    if (form.getApprover5Level() != null
        && form.getApprover5Level() == level
        && form.getApprover5() != null
        && !form.getApprover5().isEmpty()) {
      approvers.add(form.getApprover5());
    }

    if (form.getApprover6Level() != null
        && form.getApprover6Level() == level
        && form.getApprover6() != null
        && !form.getApprover6().isEmpty()) {
      approvers.add(form.getApprover6());
    }

    return approvers;
  }

  /** 获取用户在审批流程中的层级 */
  private int getCurrentApprovalLevel(ApplicationForm form, String approverUserName) {
    if (Objects.equals(form.getApprover1(), approverUserName))
      return form.getApprover1Level() != null ? form.getApprover1Level() : 0;
    if (Objects.equals(form.getApprover2(), approverUserName))
      return form.getApprover2Level() != null ? form.getApprover2Level() : 0;
    if (Objects.equals(form.getApprover3(), approverUserName))
      return form.getApprover3Level() != null ? form.getApprover3Level() : 0;
    if (Objects.equals(form.getApprover4(), approverUserName))
      return form.getApprover4Level() != null ? form.getApprover4Level() : 0;
    if (Objects.equals(form.getApprover5(), approverUserName))
      return form.getApprover5Level() != null ? form.getApprover5Level() : 0;
    if (Objects.equals(form.getApprover6(), approverUserName))
      return form.getApprover6Level() != null ? form.getApprover6Level() : 0;
    return 0;
  }

  /** 获取审批人角色 */
  private String getApproverRole(ApplicationForm form, String approverUserName) {
    if (Objects.equals(form.getApprover1(), approverUserName)) return "approver1";
    if (Objects.equals(form.getApprover2(), approverUserName)) return "approver2";
    if (Objects.equals(form.getApprover3(), approverUserName)) return "approver3";
    if (Objects.equals(form.getApprover4(), approverUserName)) return "approver4";
    if (Objects.equals(form.getApprover5(), approverUserName)) return "approver5";
    if (Objects.equals(form.getApprover6(), approverUserName)) return "approver6";
    return "unknown";
  }

  /** 判断当前层级是否全部完成 */
  // ApplicationFormServiceImpl.java
  /** 判断当前层级是否全部完成 */
  private boolean isCurrentLevelCompleted(ApplicationForm form, Integer currentRound) {
    // 获取所有审批记录
    List<Map<String, Object>> records = getApprovalRecordsFromForm(form);

    // 获取当前轮次的记录
    List<Map<String, Object>> currentRoundRecords =
        records.stream()
            .filter(
                r -> {
                  Object round = r.get("round");
                  return round != null && round == currentRound;
                })
            .collect(Collectors.toList());

    // 获取当前审批人所在层级
    int currentLevel = getCurrentApprovalLevel(form, getCurrentApproverUserName(form));

    // 检查当前层级是否全部审批通过
    long approvedCount =
        currentRoundRecords.stream()
            .filter(
                r -> {
                  Object approverLevel = r.get("approverLevel");
                  Object approvalStatus = r.get("approvalStatus");
                  return approverLevel != null
                      && approvalStatus != null
                      && (Integer) approverLevel == currentLevel
                      && (Integer) approvalStatus == ApprovalRecord.STATUS_APPROVED;
                })
            .count();

    // 获取当前层级的审批人数
    long totalCount = getApproverCountByLevel(form, currentLevel);

    return approvedCount == totalCount;
  }

  /** 获取当前审批人用户名（简化实现，实际应根据上下文获取） */
  private String getCurrentApproverUserName(ApplicationForm form) {
    // 这里只是一个示例实现，实际应从安全上下文获取当前用户
    if (form.getApprover1() != null) return form.getApprover1();
    if (form.getApprover2() != null) return form.getApprover2();
    if (form.getApprover3() != null) return form.getApprover3();
    if (form.getApprover4() != null) return form.getApprover4();
    if (form.getApprover5() != null) return form.getApprover5();
    if (form.getApprover6() != null) return form.getApprover6();
    return "";
  }

  /** 根据层级获取审批人数 */
  private long getApproverCountByLevel(ApplicationForm form, int level) {
    long count = 0;
    if (form.getApprover1Level() != null
        && form.getApprover1Level() == level
        && form.getApprover1() != null
        && !form.getApprover1().isEmpty()) count++;
    if (form.getApprover2Level() != null
        && form.getApprover2Level() == level
        && form.getApprover2() != null
        && !form.getApprover2().isEmpty()) count++;
    if (form.getApprover3Level() != null
        && form.getApprover3Level() == level
        && form.getApprover3() != null
        && !form.getApprover3().isEmpty()) count++;
    if (form.getApprover4Level() != null
        && form.getApprover4Level() == level
        && form.getApprover4() != null
        && !form.getApprover4().isEmpty()) count++;
    if (form.getApprover5Level() != null
        && form.getApprover5Level() == level
        && form.getApprover5() != null
        && !form.getApprover5().isEmpty()) count++;
    if (form.getApprover6Level() != null
        && form.getApprover6Level() == level
        && form.getApprover6() != null
        && !form.getApprover6().isEmpty()) count++;
    return count;
  }

  /** 判断是否还有更高层级需要审批 */
  private boolean hasNextLevel(ApplicationForm form, Integer currentRound) {
    // 获取所有审批记录
    List<Map<String, Object>> records = getApprovalRecordsFromForm(form);

    // 获取当前轮次的记录
    List<Map<String, Object>> currentRoundRecords =
        records.stream()
            .filter(
                r -> {
                  Object round = r.get("round");
                  return round != null && round == currentRound;
                })
            .collect(Collectors.toList());

    // 获取当前最高层级
    int maxLevel =
        currentRoundRecords.stream()
            .mapToInt(
                r -> {
                  Object approverLevel = r.get("approverLevel");
                  return approverLevel != null ? (Integer) approverLevel : 0;
                })
            .max()
            .orElse(0);

    // 检查是否有更高层级的审批人
    return hasHigherLevelApprover(form, maxLevel);
  }

  /** 判断是否有更高层级的审批人 */
  private boolean hasHigherLevelApprover(ApplicationForm form, int currentLevel) {
    return (form.getApprover1Level() != null && form.getApprover1Level() > currentLevel)
        || (form.getApprover2Level() != null && form.getApprover2Level() > currentLevel)
        || (form.getApprover3Level() != null && form.getApprover3Level() > currentLevel)
        || (form.getApprover4Level() != null && form.getApprover4Level() > currentLevel)
        || (form.getApprover5Level() != null && form.getApprover5Level() > currentLevel)
        || (form.getApprover6Level() != null && form.getApprover6Level() > currentLevel);
  }

  /** 获取审批人总数 */
  private long getApproverCount(ApplicationForm form) {
    long count = 0;
    if (form.getApprover1() != null && !form.getApprover1().isEmpty()) count++;
    if (form.getApprover2() != null && !form.getApprover2().isEmpty()) count++;
    if (form.getApprover3() != null && !form.getApprover3().isEmpty()) count++;
    if (form.getApprover4() != null && !form.getApprover4().isEmpty()) count++;
    if (form.getApprover5() != null && !form.getApprover5().isEmpty()) count++;
    if (form.getApprover6() != null && !form.getApprover6().isEmpty()) count++;
    return count;
  }

  // 添加自动处理流程启动方法
  private void startAutoProcess(Integer applicationFormId) {
    ApplicationForm form =
        applicationFormRepository
            .findById(applicationFormId)
            .orElseThrow(() -> new RuntimeException("申请单不存在"));

    // 只有新增、修改、上线、下线申请才需要自动处理
    if (form.getApplicationType() >= 0 && form.getApplicationType() <= 3) {
      form.setStatus(ApplicationForm.STATUS_AUTO_PROCESSING);
      applicationFormRepository.save(form);

      // 异步执行自动处理流程
      completeProcessBasedOnType(form);
    } else {
      // 其他类型直接完成
      form.setStatus(ApplicationForm.STATUS_COMPLETED);
      applicationFormRepository.save(form);
    }
  }

  @Override
  public List<ApprovalRecordDto> getApprovalHistory(Integer applicationFormId) {
    ApplicationForm form = applicationFormRepository.findById(applicationFormId).orElse(null);
    if (form == null || form.getApprovalRecords() == null) {
      return new ArrayList<>();
    }

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      List<Map<String, Object>> records =
          objectMapper.readValue(
              form.getApprovalRecords(), new TypeReference<List<Map<String, Object>>>() {});

      return records.stream()
          .map(
              record -> {
                ApprovalRecordDto dto = new ApprovalRecordDto();
                dto.setId(record.get("id") != null ? record.get("id").hashCode() : 0);
                dto.setApplicationFormId((Integer) record.get("applicationFormId"));
                dto.setRound((Integer) record.get("round"));
                dto.setStepOrder((Integer) record.get("approverLevel"));
                dto.setApproverRole((String) record.get("approverRole"));
                dto.setApproverUserName((String) record.get("approverUserName"));
                dto.setApprovalStatus((Integer) record.get("approvalStatus"));
                dto.setComment((String) record.get("comment"));
                dto.setApprovedAt((Timestamp) record.get("approvedAt"));

                Object approvalStatus = record.get("approvalStatus");
                if (approvalStatus != null) {
                  switch ((Integer) approvalStatus) {
                    case STATUS_PEDING:
                      dto.setApprovalStatusText("待审批");
                      break;
                    case STATUS_APPROVED:
                      dto.setApprovalStatusText("通过");
                      break;
                    case STATUS_REJECTED:
                      dto.setApprovalStatusText("驳回");
                      break;
                    default:
                      dto.setApprovalStatusText("未知");
                  }
                }

                return dto;
              })
          .collect(Collectors.toList());
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  // 申请通过后数据持久化处理
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void ApplicationPostProcess(Integer applicationFormId) {
    ApplicationForm form =
        applicationFormRepository
            .findById(applicationFormId)
            .orElseThrow(() -> new RuntimeException("申请单不存在"));

    // 根据申请数据类型处理不同类型的申请
    switch (form.getApplicationDataType()) {
      case APPLICATION_DATATYPE_DEVICEINFO:
        processApplicationData(form, deviceInfoService);
        break;
      case APPLICATION_DATATYPE_GATEWAYINFO:
        processApplicationData(form, gatewayInfoService);
        break;
    }
  }

  private void processApplicationData(ApplicationForm form, DataInfoService dataInfoService) {
    switch (form.getApplicationType()) {
      case APPLICATION_TYPE_ADD: // 新增
        Integer applicationDataId = dataInfoService.createFromJson(form.getDataDetails());
        // 更新申请单的application_data_id
        form.setApplicationDataId(applicationDataId);
        // 检查application_data_id是否已经存在，如果存在就不更新
        if (applicationDataId != null
            && applicationFormRepository.findByApplicationDataId(applicationDataId) == null) {
          applicationFormRepository.save(form);
        }
        break;
      case APPLICATION_TYPE_EDIT: // 修改
        dataInfoService.updateFromJson(form.getDataDetails(), form.getApplicationDataId());
        break;
      case APPLICATION_TYPE_ONLINE: // 上线
        dataInfoService.setDataStatus(form.getApplicationDataId(), GatewayInfo.STATUS_ONLINE);
        break;
      case APPLICATION_TYPE_OFFLINE: // 下线
        dataInfoService.setDataStatus(form.getApplicationDataId(), GatewayInfo.STATUS_OFFLINE);
        break;
      default:
        throw new RuntimeException("不支持的申请类型");
    }
  }

  // 在 ApplicationFormServiceImpl.java 中添加以下方法
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void saveDraft(ApplicationFormVo vo) {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    ApplicationForm form;

    if (vo.getId() == null) {
      // 新建草稿
      form = new ApplicationForm();
      form.setUuid(generateApplicationNo());
      form.setStatus(ApplicationForm.STATUS_DRAFT); // 草稿状态
      form.setCreatedAt(now);
    } else {
      // 更新草稿
      form =
          applicationFormRepository
              .findById(vo.getId())
              .orElseThrow(() -> new RuntimeException("申请单不存在"));

      // 只能更新草稿、已撤回、已驳回状态的申请单
      if (!form.getStatus().equals(ApplicationForm.STATUS_DRAFT)
          && !form.getStatus().equals(ApplicationForm.STATUS_WITHDRAWN)
          && !form.getStatus().equals(ApplicationForm.STATUS_REJECTED)) {
        throw new RuntimeException("只能更新草稿、已撤回、已驳回状态的申请单");
      }
    }

    form.setUpdatedAt(now);
    // 映射VO到Entity
    mapVoToEntity(form, vo);
    ApplicationForm save = applicationFormRepository.save(form);

    // TODO 测试用，稍后要删除 处理设备信息申请
    // 存储到数据库
    ApplicationPostProcess(save.getId());
    // executeSync(save);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void manualTriggerFirmwareVerify(Integer applicationFormId) {
    ApplicationForm form =
        applicationFormRepository
            .findById(applicationFormId)
            .orElseThrow(() -> new RuntimeException("申请单不存在"));

    // 检查是否可以手动触发固件校验
    if (form.getStatus() != ApplicationForm.STATUS_FIRMWARE_FAILED
        && form.getStatus() != ApplicationForm.STATUS_AUTO_FAILED) {
      throw new RuntimeException("当前状态不允许手动触发固件校验");
    }

    // 更新状态为手动触发
    form.setStatus(ApplicationForm.STATUS_MANUAL_TRIGGERED);
    form.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    applicationFormRepository.save(form);

    // 异步执行固件校验
    executeFirmwareVerification(form);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void manualTriggerSync(Integer applicationFormId) {
    ApplicationForm form =
        applicationFormRepository
            .findById(applicationFormId)
            .orElseThrow(() -> new RuntimeException("申请单不存在"));

    // 检查是否可以手动触发同步
    if (form.getStatus() != ApplicationForm.STATUS_SYNC_FAILED
        && form.getStatus() != ApplicationForm.STATUS_AUTO_FAILED) {
      throw new RuntimeException("当前状态不允许手动触发同步");
    }

    // 更新状态为手动触发
    form.setStatus(ApplicationForm.STATUS_MANUAL_TRIGGERED);
    form.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    applicationFormRepository.save(form);

    // 异步执行同步
    executeSync(form);
  }

  // 固件校验预留方法
  private void executeFirmwareVerification(ApplicationForm form) {
    // TODO: 实现固件校验逻辑
    // 这里可以调用实际的固件校验服务

    try {
      // 模拟固件校验过程
      // firmwareVerificationService.verify(form);

      // 校验成功后进入同步阶段
      form.setStatus(ApplicationForm.STATUS_SYNCING);
      applicationFormRepository.save(form);

      // 异步执行同步
      executeSync(form);
    } catch (Exception e) {
      // 固件校验失败
      form.setStatus(ApplicationForm.STATUS_FIRMWARE_FAILED);
      applicationFormRepository.save(form);
      // 记录错误日志
      // log.error("固件校验失败", e);
    }
  }

  // 在 ApplicationFormServiceImpl.java 中修改 executeSync 方法
  /** 同步预留方法 */
  private void executeSync(ApplicationForm form) {
    try {
      // 根据申请单数据类型执行不同的同步逻辑
      if (form.getApplicationDataType().equals(APPLICATION_DATATYPE_DEVICEINFO)) { // deviceInfo类型
        // 调用设备信息服务的同步方法
        deviceInfoService.syncDeviceInfo(
            form.getApplicationDataId(),
            form.getApplicationType() == APPLICATION_TYPE_ONLINE
                ? DeviceInfo.STATUS_ONLINE
                : DeviceInfo.STATUS_OFFLINE);

        // 同步成功后完成流程
        form.setStatus(ApplicationForm.STATUS_COMPLETED);
        applicationFormRepository.save(form);
      } else if (form.getApplicationDataType()
          .equals(APPLICATION_DATATYPE_GATEWAYINFO)) { // gatewayInfo类型
        // 保持原有的网关信息同步逻辑
        // TODO: 实现网关信息同步逻辑
        form.setStatus(ApplicationForm.STATUS_COMPLETED);
        applicationFormRepository.save(form);
      } else {
        throw new RuntimeException("不支持的申请数据类型");
      }
    } catch (Exception e) {
      // 同步失败
      form.setStatus(ApplicationForm.STATUS_SYNC_FAILED);
      applicationFormRepository.save(form);
      // 记录错误日志
      // log.error("同步失败", e);
    }
  }

  // 根据申请类型完成流程
  // 根据申请类型完成流程
  private void completeProcessBasedOnType(ApplicationForm form) {
    switch (form.getApplicationType()) {
      case APPLICATION_TYPE_ADD: // 新增
      case APPLICATION_TYPE_EDIT: // 修改
        // 新增和修改需要固件校验和同步
        form.setStatus(ApplicationForm.STATUS_FIRMWARE_VERIFY);
        applicationFormRepository.save(form);
        executeFirmwareVerification(form);
        break;
      case APPLICATION_TYPE_ONLINE: // 上线
      case APPLICATION_TYPE_OFFLINE: // 下线
        // 上线和下线只需要同步
        form.setStatus(ApplicationForm.STATUS_SYNCING);
        applicationFormRepository.save(form);
        executeSync(form);
        break;
      default:
        throw new RuntimeException("不支持的申请类型");
    }
  }

  // 更新设备信息状态
  // 修改 ApplicationFormServiceImpl.java 中的 updateDeviceInfoStatus 方法
  private void updateDeviceInfoStatus(ApplicationForm form) {
    if (form.getApplicationType().equals(APPLICATION_TYPE_ONLINE)) { // 上线申请
      deviceInfoService.setDataStatus(
          form.getApplicationDataId(), DeviceInfo.STATUS_ONLINE); // 设置为上线
    } else if (form.getApplicationType().equals(APPLICATION_TYPE_OFFLINE)) { // 下线申请
      deviceInfoService.setDataStatus(
          form.getApplicationDataId(), DeviceInfo.STATUS_OFFLINE); // 设置为下线
    }
    // 新增和修改申请默认已经是上线状态
  }

  // 在 ApplicationFormServiceImpl.java 中替换之前的 deleteApplicationForm 方法
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteApplicationForm(Integer applicationFormId, String applicantName) {
    ApplicationForm form =
        applicationFormRepository
            .findById(applicationFormId)
            .orElseThrow(() -> new RuntimeException("申请单不存在"));

    // 检查申请人是否匹配
    if (!form.getApplicantUserName().equals(applicantName)) {
      throw new RuntimeException("只有申请人才能删除申请单");
    }

    // 检查当前状态是否允许删除（只有草稿状态才能删除）
    if (!form.getStatus().equals(ApplicationForm.STATUS_DRAFT)) {
      throw new RuntimeException("只有草稿状态的申请单才能删除");
    }

    // 删除申请单本身（审批记录已包含在内）
    applicationFormRepository.deleteById(applicationFormId);
  }
}
