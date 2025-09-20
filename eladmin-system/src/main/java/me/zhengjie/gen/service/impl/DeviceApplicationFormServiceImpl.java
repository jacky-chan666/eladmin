/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.gen.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.zhengjie.gen.domain.ApprovalRecord;
import me.zhengjie.gen.domain.CurrentApprovalStatus;
import me.zhengjie.gen.domain.DeviceApplicationForm;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.gen.domain.DeviceInfo;
import me.zhengjie.gen.domain.vo.DeviceApplicationFormVo;
import me.zhengjie.gen.repository.ApprovalRecordRepository;
import me.zhengjie.gen.repository.CurrentApprovalStatusRepository;
import me.zhengjie.gen.repository.DeviceInfoRepository;
import me.zhengjie.gen.service.DeviceInfoService;
import me.zhengjie.gen.service.dto.ApprovalRecordDto;
import me.zhengjie.gen.service.dto.PendingApprovalDto;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.gen.repository.DeviceApplicationFormRepository;
import me.zhengjie.gen.service.DeviceApplicationFormService;
import me.zhengjie.gen.service.dto.DeviceApplicationFormDto;
import me.zhengjie.gen.service.dto.DeviceApplicationFormQueryCriteria;
import me.zhengjie.gen.service.mapstruct.DeviceApplicationFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.utils.PageResult;

/**
 * @website https://eladmin.vip
 * @description 服务实现
 * @author Chen Jiayuan
 * @date 2025-09-18
 **/
@Service
@RequiredArgsConstructor
public class DeviceApplicationFormServiceImpl implements DeviceApplicationFormService {

    private final DeviceApplicationFormRepository deviceApplicationFormRepository;
    private final CurrentApprovalStatusRepository currentApprovalStatusRepository;
    private final ApprovalRecordRepository approvalRecordRepository;
    private final DeviceApplicationFormMapper deviceApplicationFormMapper;
    private final DeviceInfoRepository deviceInfoRepository;
    private final DeviceInfoService deviceInfoService;

    // 修改 DeviceApplicationFormServiceImpl.java
    @Override
    public PageResult<DeviceApplicationFormDto> queryAll(DeviceApplicationFormQueryCriteria criteria, Pageable pageable) {
        Page<DeviceApplicationForm> page = deviceApplicationFormRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);

        // 转换为 DTO 并添加审批信息
        Page<DeviceApplicationFormDto> dtoPage = page.map(deviceApplicationForm -> {
            DeviceApplicationFormDto dto = deviceApplicationFormMapper.toDto(deviceApplicationForm);
            // 添加审批信息
            enrichApprovalInfo(dto, deviceApplicationForm.getId());
            return dto;
        });

        return PageUtil.toPage(dtoPage);
    }

    @Override
    public List<DeviceApplicationFormDto> queryAll(DeviceApplicationFormQueryCriteria criteria) {
        List<DeviceApplicationForm> forms = deviceApplicationFormRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        // 转换为 DTO 并添加审批信息
        return forms.stream().map(form -> {
            DeviceApplicationFormDto dto = deviceApplicationFormMapper.toDto(form);
            // 添加审批信息
            enrichApprovalInfo(dto, form.getId());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 丰富申请单的审批信息
     *
     * @param dto               申请单 DTO
     * @param applicationFormId 申请单 ID
     */
    private void enrichApprovalInfo(DeviceApplicationFormDto dto, Integer applicationFormId) {
        // 获取历史审批记录并转换为 JSON 字符串
        List<ApprovalRecord> approvalRecords = approvalRecordRepository.findByApplicationFormIdOrderByRoundAscStepOrderAscApprovedAtAsc(applicationFormId);

        // 将所有审批记录序列化为 JSON 字符串存储到 approvalHistory 字段
        try {
            List<Map<String, Object>> historyList = approvalRecords.stream().map(record -> {
                Map<String, Object> recordMap = new LinkedHashMap<>();
                recordMap.put("id", record.getId());
                recordMap.put("applicationFormId", record.getApplicationFormId());
                recordMap.put("round", record.getRound());
                recordMap.put("stepOrder", record.getStepOrder());
                recordMap.put("approverRole", record.getApproverRole());
                recordMap.put("approverUserName", record.getApproverUserName());
                recordMap.put("approvalStatus", record.getApprovalStatus());
                recordMap.put("comment", record.getComment());
                recordMap.put("approvedAt", record.getApprovedAt());
                return recordMap;
            }).collect(Collectors.toList());

            String historyJson = new ObjectMapper().writeValueAsString(historyList);
            dto.setApprovalHistory(historyJson);
        } catch (Exception e) {
            // 如果序列化失败，保持原有值或设置为空字符串
            dto.setApprovalHistory("[]");
        }

        // 获取最新轮次
        Integer maxRound = approvalRecordRepository.findMaxRoundByApplicationFormId(applicationFormId);

        // 如果没有审批记录，则所有审批状态字段保持默认值（null）
        if (maxRound > 0) {
            // 只处理最新轮次的审批记录
            List<ApprovalRecord> latestRoundRecords = approvalRecords.stream()
                    .filter(record -> record.getRound().equals(maxRound))
                    .collect(Collectors.toList());

            // 将最新轮次审批记录信息填充到对应的审批状态和意见字段中
            for (ApprovalRecord record : latestRoundRecords) {
                switch (record.getApproverRole()) {
                    case "test_contact":
                        dto.setTestContactApproval(record.getApprovalStatus());
                        dto.setTestContactComment(record.getComment());
                        break;
                    case "test_leader":
                        dto.setTestLeaderApproval(record.getApprovalStatus());
                        dto.setTestLeaderComment(record.getComment());
                        break;
                    case "dev_contact":
                        dto.setDevContactApproval(record.getApprovalStatus());
                        dto.setDevContactComment(record.getComment());
                        break;
                    case "dev_leader":
                        dto.setDevLeaderApproval(record.getApprovalStatus());
                        dto.setDevLeaderComment(record.getComment());
                        break;
                }
            }
        }
    }

    // 修改 DeviceApplicationFormServiceImpl.java 中的 findById 方法
    @Override
    @Transactional
    public DeviceApplicationFormDto findById(Integer id) {
        DeviceApplicationForm deviceApplicationForm = deviceApplicationFormRepository.findById(id).orElseGet(DeviceApplicationForm::new);
        ValidationUtil.isNull(deviceApplicationForm.getId(), "DeviceApplicationForm", "id", id);

        DeviceApplicationFormDto dto = deviceApplicationFormMapper.toDto(deviceApplicationForm);
        // 添加审批信息
        enrichApprovalInfo(dto, id);

        return dto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DeviceApplicationForm resources) {
        if (deviceApplicationFormRepository.findByUuid(resources.getUuid()) != null) {
            throw new EntityExistException(DeviceApplicationForm.class, "uuid", resources.getUuid());
        }
        if (deviceApplicationFormRepository.findByApplicationDataId(resources.getApplicationDataId()) != null) {
            throw new EntityExistException(DeviceApplicationForm.class, "application_data_id", resources.getApplicationDataId().toString());
        }
        deviceApplicationFormRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceApplicationForm resources) {
        DeviceApplicationForm deviceApplicationForm = deviceApplicationFormRepository.findById(resources.getId()).orElseGet(DeviceApplicationForm::new);
        ValidationUtil.isNull(deviceApplicationForm.getId(), "DeviceApplicationForm", "id", resources.getId());
        DeviceApplicationForm deviceApplicationForm1 = null;
        deviceApplicationForm1 = deviceApplicationFormRepository.findByUuid(resources.getUuid());
        if (deviceApplicationForm1 != null && !deviceApplicationForm1.getId().equals(deviceApplicationForm.getId())) {
            throw new EntityExistException(DeviceApplicationForm.class, "uuid", resources.getUuid());
        }
        deviceApplicationForm1 = deviceApplicationFormRepository.findByApplicationDataId(resources.getApplicationDataId());
        if (deviceApplicationForm1 != null && !deviceApplicationForm1.getId().equals(deviceApplicationForm.getId())) {
            throw new EntityExistException(DeviceApplicationForm.class, "application_data_id", resources.getApplicationDataId().toString());
        }
        deviceApplicationForm.copy(resources);
        deviceApplicationFormRepository.save(deviceApplicationForm);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            deviceApplicationFormRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DeviceApplicationFormDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeviceApplicationFormDto deviceApplicationForm : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("申请单UUID", deviceApplicationForm.getUuid());
            map.put("申请人姓名", deviceApplicationForm.getApplicantUserName());
            map.put("所属部门", deviceApplicationForm.getDepartment());
            map.put("申请数据id", deviceApplicationForm.getApplicationDataId());
            map.put("申请单类型：新增，修改，上线，下线", deviceApplicationForm.getApplicationType());
            map.put("申请单数据类型：omada，vigi，adblocking", deviceApplicationForm.getApplicationDataType());
            map.put("申请单标题", deviceApplicationForm.getApplicationTitle());
            map.put("申请理由", deviceApplicationForm.getApplicationReason());
            map.put("申请状态", deviceApplicationForm.getStatus());
            map.put("测试接口人", deviceApplicationForm.getTestContact());
            map.put("测试组长", deviceApplicationForm.getTestLeader());
            map.put("研发接口人", deviceApplicationForm.getDevContact());
            map.put("研发组长", deviceApplicationForm.getDevLeader());
            map.put("测试接口人审批状态", deviceApplicationForm.getTestContactApproval());
            map.put("测试组长审批状态", deviceApplicationForm.getTestLeaderApproval());
            map.put("研发接口人审批状态", deviceApplicationForm.getDevContactApproval());
            map.put("研发组长审批状态", deviceApplicationForm.getDevLeaderApproval());
            map.put("测试接口人审批意见", deviceApplicationForm.getTestContactComment());
            map.put("测试组长审批意见", deviceApplicationForm.getTestLeaderComment());
            map.put("研发接口人审批意见", deviceApplicationForm.getDevContactComment());
            map.put("研发组长意见审批", deviceApplicationForm.getDevLeaderComment());
            map.put("当前审核人列表（JSON格式存储）", deviceApplicationForm.getCurrentApprovers());
            map.put("审核历史表（JSON格式存储，记录每次提交的审批人，审批状态和审批意见）", deviceApplicationForm.getApprovalHistory());
            map.put(" createdAt", deviceApplicationForm.getCreatedAt());
            map.put(" updatedAt", deviceApplicationForm.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    // 修改 DeviceApplicationFormServiceImpl.java 中的 submitApplication 方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitApplication(DeviceApplicationFormVo vo) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // 查询是否已存在该申请单（根据 id 判断）
        DeviceApplicationForm form;
        boolean isNew = vo.getId() == null;

        if (isNew) {
            form = new DeviceApplicationForm();
            form.setStatus(DeviceApplicationForm.STATUS_SUBMITTED); // 已提交（修改为新状态）
            form.setUuid(generateApplicationNo());
            form.setCreatedAt(now);
            form.setUpdatedAt(now);
        } else {
            form = deviceApplicationFormRepository.findById(vo.getId())
                    .orElseThrow(() -> new RuntimeException("申请单不存在"));
            // 更新模式：复制新数据并设置为已提交状态
            form.setUpdatedAt(now);
            form.setStatus(DeviceApplicationForm.STATUS_SUBMITTED); // 已提交（修改为新状态）
        }

        // === 手动映射 VO 字段到 Entity ===
        mapVoToEntity(form, vo);

        // 保存申请单
        deviceApplicationFormRepository.save(form);

        // 获取申请单 ID
        Integer applicationFormId = form.getId();

        // 确定当前审批轮次
        Integer round = approvalRecordRepository.findMaxRoundByApplicationFormId(applicationFormId) + 1;

        // 清除上一轮未完成的审批状态（防止重复）
        currentApprovalStatusRepository.deleteByApplicationFormIdAndRound(applicationFormId, round - 1);

        // 初始化第一阶段审批人（并行审批）
        List<CurrentApprovalStatus> initialApprovals = new ArrayList<>();

        // 测试接口人
        CurrentApprovalStatus testContactStatus = new CurrentApprovalStatus();
        testContactStatus.setApplicationFormId(applicationFormId);
        testContactStatus.setRound(round);
        testContactStatus.setStepOrder(CurrentApprovalStatus.STEP_ONE);
        testContactStatus.setApproverRole("test_contact");
        testContactStatus.setApproverUserName(vo.getTestContact());
        testContactStatus.setStatus(CurrentApprovalStatus.STATUS_PEDING); // 待审批
        initialApprovals.add(testContactStatus);

        // 研发接口人
        CurrentApprovalStatus devContactStatus = new CurrentApprovalStatus();
        devContactStatus.setApplicationFormId(applicationFormId);
        devContactStatus.setRound(round);
        devContactStatus.setStepOrder(CurrentApprovalStatus.STEP_ONE);
        devContactStatus.setApproverRole("dev_contact");
        devContactStatus.setApproverUserName(vo.getDevContact());
        devContactStatus.setStatus(CurrentApprovalStatus.STATUS_PEDING); // 待审批
        initialApprovals.add(devContactStatus);

        // 保存初始审批状态
        currentApprovalStatusRepository.saveAll(initialApprovals);
    }

    /**
     * 生成申请单编号
     * 格式: APP + 年月日 + 6位序列号 (例如: APP20250918000001)
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


    /**
     * 将 VO 中的数据复制到 JPA Entity
     */
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson

    private void mapVoToEntity(DeviceApplicationForm entity, DeviceApplicationFormVo vo) {
        // 基础字段（标题、理由等）
        entity.setApplicationTitle(vo.getApplicationTitle());
        entity.setApplicationReason(vo.getApplicationReason());
        entity.setApplicationType(vo.getApplicationType());
        entity.setApplicationDataType(vo.getApplicationDataType());
        entity.setTestContact(vo.getTestContact());
        entity.setTestLeader(vo.getTestLeader());
        entity.setDevContact(vo.getDevContact());
        entity.setDevLeader(vo.getDevLeader());

        // 申请人信息
        entity.setApplicantUserName(vo.getApplicantUserName());

        // === 关键修改：构建设备信息详情 JSON ===

        // 直接存储设备详细信息JSON字符串
        entity.setDeviceInfoDetails(vo.getDeviceDetail());
    }

    // 修改 DeviceApplicationFormServiceImpl.java 中的 withdrawApplication 方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawApplication(Integer applicationFormId, String applicantName) {
        DeviceApplicationForm form = deviceApplicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new RuntimeException("申请单不存在"));

        // 检查申请人是否匹配
        if (!form.getApplicantUserName().equals(applicantName)) {
            throw new RuntimeException("只有申请人才能撤回申请单");
        }

        // 检查当前状态是否允许撤回（已提交、固件校验失败、同步失败状态下才能撤回）
        if (!form.getStatus().equals(DeviceApplicationForm.STATUS_SUBMITTED) &&
                !form.getStatus().equals(DeviceApplicationForm.STATUS_FIRMWARE_FAILED) &&
                !form.getStatus().equals(DeviceApplicationForm.STATUS_SYNC_FAILED)) {
            throw new RuntimeException("当前状态不允许撤回申请单");
        }

        // 更新状态为已撤回
        form.setStatus(DeviceApplicationForm.STATUS_WITHDRAWN);
        form.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        deviceApplicationFormRepository.save(form);

        // 清除当前审批状态
        Integer round = approvalRecordRepository.findMaxRoundByApplicationFormId(applicationFormId);
        if (round > 0) {
            currentApprovalStatusRepository.deleteByApplicationFormIdAndRound(applicationFormId, round);
        }

        // 删除相关的审批记录
        // 注意：这里根据业务需求决定是否删除审批记录，如果需要保留历史记录则不删除
        // approvalRecordRepository.deleteByApplicationFormId(applicationFormId);
    }

    // 修改 DeviceApplicationFormServiceImpl.java 中的 getPendingApprovals 方法（非分页版本）
    @Override
    public List<PendingApprovalDto> getPendingApprovals(String approverUserName) {
        List<CurrentApprovalStatus> pendingStatuses = currentApprovalStatusRepository
                .findByApproverUserNameAndStatusOrderByApplicationFormId(approverUserName, CurrentApprovalStatus.STATUS_PEDING);

        List<PendingApprovalDto> result = new ArrayList<>();
        for (CurrentApprovalStatus status : pendingStatuses) {
            Optional<DeviceApplicationForm> formOpt = deviceApplicationFormRepository
                    .findById(status.getApplicationFormId());

            if (formOpt.isPresent()) {
                DeviceApplicationForm form = formOpt.get();
                // 只保留状态为"已提交"或"待审批"的申请单
                if (form.getStatus().equals(DeviceApplicationForm.STATUS_SUBMITTED) ||
                        form.getStatus().equals(DeviceApplicationForm.STATUS_PENDING)) {
                    DeviceApplicationFormDto formDto = deviceApplicationFormMapper.toDto(form);
                    enrichApprovalInfo(formDto, form.getId());
                    PendingApprovalDto dto = new PendingApprovalDto();
                    dto.setApplicationForm(formDto);
                    dto.setRound(status.getRound());
                    dto.setStepOrder(status.getStepOrder());
                    result.add(dto);
                }
            }
        }

        return result;
    }


    // 修改 DeviceApplicationFormServiceImpl.java 中的 getPendingApprovals 方法（分页版本）
    @Override
    @Transactional(readOnly = true)
    public PageResult<PendingApprovalDto> getPendingApprovals(DeviceApplicationFormQueryCriteria criteria, Pageable pageable) {
        // Step 1: 构建查询条件 —— 查找 status=0 的 CurrentApprovalStatus
        List<CurrentApprovalStatus> pendingStatuses = currentApprovalStatusRepository
                .findByApproverUserNameAndStatusOrderByApplicationFormId(criteria.getApproverUserName(), CurrentApprovalStatus.STATUS_PEDING);

        // Step 2: 过滤有效的申请单（状态必须为已提交或待审批）
        List<PendingApprovalDto> result = pendingStatuses.stream()
                .map(status -> {
                    Optional<DeviceApplicationForm> formOpt = deviceApplicationFormRepository
                            .findById(status.getApplicationFormId());

                    if (formOpt.isPresent()) {
                        DeviceApplicationForm form = formOpt.get();
                        DeviceApplicationFormDto formDto = deviceApplicationFormMapper.toDto(form);
                        enrichApprovalInfo(formDto, form.getId());
                        // 只保留状态为"已提交"或"待审批"的申请单
                        if (form.getStatus().equals(DeviceApplicationForm.STATUS_SUBMITTED) ||
                                form.getStatus().equals(DeviceApplicationForm.STATUS_PENDING)) {
                            PendingApprovalDto dto = new PendingApprovalDto();
                            dto.setApplicationForm(formDto);
                            dto.setRound(status.getRound());
                            dto.setStepOrder(status.getStepOrder());
                            return dto;
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Step 3: 手动分页
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


    // 修改 DeviceApplicationFormServiceImpl.java 中的 getApprovedApplications 方法
    @Override
    @Transactional(readOnly = true)
    public PageResult<DeviceApplicationFormDto> getApprovedApplications(DeviceApplicationFormQueryCriteria criteria, Pageable pageable) {
        List<ApprovalRecord> records = approvalRecordRepository
                .findByApproverUserNameAndApprovalStatusInOrderByApprovedAtDesc(
                        criteria.getApproverUserName(), Arrays.asList(ApprovalRecord.STATUS_APPROVED, ApprovalRecord.STATUS_REJECTED));

        // 去重，只保留每个申请单的最新审批记录
        Map<Integer, ApprovalRecord> latestApprovals = new LinkedHashMap<>();
        for (ApprovalRecord record : records) {
            if (!latestApprovals.containsKey(record.getApplicationFormId()) ||
                    record.getApprovedAt().after(latestApprovals.get(record.getApplicationFormId()).getApprovedAt())) {
                latestApprovals.put(record.getApplicationFormId(), record);
            }
        }

        // 转换为 DeviceApplicationFormDto 列表
        List<DeviceApplicationFormDto> result = new ArrayList<>();
        for (ApprovalRecord record : latestApprovals.values()) {
            Optional<DeviceApplicationForm> formOpt = deviceApplicationFormRepository
                    .findById(record.getApplicationFormId());
            if (formOpt.isPresent()) {
                DeviceApplicationFormDto dto = deviceApplicationFormMapper.toDto(formOpt.get());
                enrichApprovalInfo(dto, record.getApplicationFormId());
                result.add(dto);
            }
        }

        // 应用查询条件过滤
        List<DeviceApplicationFormDto> filteredResult = result.stream()
                .filter(dto -> {
                    boolean match = true;

                    // 根据申请单UUID过滤
                    if (criteria.getUuid() != null && !criteria.getUuid().isEmpty()) {
                        match = match && dto.getUuid() != null && dto.getUuid().contains(criteria.getUuid());
                    }

                    // 根据申请人姓名过滤
                    if (criteria.getApplicantUserName() != null && !criteria.getApplicantUserName().isEmpty()) {
                        match = match && dto.getApplicantUserName() != null && dto.getApplicantUserName().contains(criteria.getApplicantUserName());
                    }

                    // 根据申请单类型过滤
                    if (criteria.getApplicationType() != null) {
                        match = match && dto.getApplicationType() != null && dto.getApplicationType().equals(criteria.getApplicationType());
                    }

                    // 根据申请单数据类型过滤
                    if (criteria.getApplicationDataType() != null) {
                        match = match && dto.getApplicationDataType() != null && dto.getApplicationDataType().equals(criteria.getApplicationDataType());
                    }

                    // 根据申请单标题过滤
                    if (criteria.getApplicationTitle() != null && !criteria.getApplicationTitle().isEmpty()) {
                        match = match && dto.getApplicationTitle() != null && dto.getApplicationTitle().contains(criteria.getApplicationTitle());
                    }

                    return match;
                })
                .collect(Collectors.toList());

        // 手动分页
        int total = filteredResult.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<DeviceApplicationFormDto> pagedContent = new ArrayList<>();
        if (start < end) {
            pagedContent = filteredResult.subList(start, end);
        }

        // 构造 PageResult（使用项目中定义的字段）
        PageResult<DeviceApplicationFormDto> pageResult = new PageResult<>();
        pageResult.setContent(pagedContent);
        pageResult.setTotalElements(total);

        return pageResult;
    }


    // 修改 DeviceApplicationFormServiceImpl.java 中的 approveApplication 方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveApplication(Integer applicationFormId, String approverUserName,
                                   Integer approvalStatus, String comment) {
        Optional<DeviceApplicationForm> formOpt = deviceApplicationFormRepository.findById(applicationFormId);
        if (!formOpt.isPresent()) {
            throw new RuntimeException("申请单不存在");
        }

        DeviceApplicationForm form = formOpt.get();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // 检查申请单状态是否为已提交或待审批
        if (!form.getStatus().equals(DeviceApplicationForm.STATUS_SUBMITTED) &&
                !form.getStatus().equals(DeviceApplicationForm.STATUS_PENDING)) {
            throw new RuntimeException("申请单状态不正确，无法审批");
        }

        // 查找当前审批状态
        Integer maxRound = approvalRecordRepository.findMaxRoundByApplicationFormId(applicationFormId);
        Integer currentRound = (maxRound == null || maxRound == 0) ? 1 : maxRound;

        List<CurrentApprovalStatus> currentStatuses = currentApprovalStatusRepository
                .findByApplicationFormIdAndRound(applicationFormId, currentRound);

        CurrentApprovalStatus currentStatus = currentStatuses.stream()
                .filter(s -> s.getApproverUserName().equals(approverUserName) &&
                        s.getStatus().equals(CurrentApprovalStatus.STATUS_PEDING))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("当前用户没有待审批的任务"));

        // 如果申请单状态是已提交，将其改为待审批
        if (form.getStatus().equals(DeviceApplicationForm.STATUS_SUBMITTED)) {
            form.setStatus(DeviceApplicationForm.STATUS_PENDING);
            deviceApplicationFormRepository.save(form);
        }

        // 创建审批记录
        ApprovalRecord record = new ApprovalRecord();
        record.setApplicationFormId(applicationFormId);
        record.setRound(currentStatus.getRound());
        record.setStepOrder(currentStatus.getStepOrder());
        record.setApproverRole(currentStatus.getApproverRole());
        record.setApproverUserName(approverUserName);
        record.setApprovalStatus(approvalStatus);
        record.setComment(comment);
        record.setApprovedAt(now);
        approvalRecordRepository.save(record);

        // 更新当前审批状态
        currentStatus.setStatus(CurrentApprovalStatus.STATUS_COMPLETED); // 已审批
        currentApprovalStatusRepository.save(currentStatus);

        if (Objects.equals(approvalStatus, ApprovalRecord.STATUS_REJECTED)) {
            // 审批被驳回，更新申请单状态
            form.setStatus(DeviceApplicationForm.STATUS_REJECTED); // 已驳回
            deviceApplicationFormRepository.save(form);

            // 清除当前审批状态
            currentApprovalStatusRepository.deleteByApplicationFormIdAndRound(
                    applicationFormId, currentStatus.getRound());
        } else {
            // 审批通过，检查当前步骤是否全部完成
            Integer pendingCount = currentApprovalStatusRepository
                    .countPendingByApplicationFormIdAndRoundAndStepOrder(
                            applicationFormId, currentStatus.getRound(), currentStatus.getStepOrder());

            if (pendingCount == 0) {
                // 当前步骤全部完成，检查是否还有后续步骤
                if (Objects.equals(currentStatus.getStepOrder(), CurrentApprovalStatus.STEP_ONE)) {
                    // 进入第二阶段审批
                    List<CurrentApprovalStatus> nextApprovals = new ArrayList<>();

                    // 添加测试组长
                    CurrentApprovalStatus testLeaderStatus = new CurrentApprovalStatus();
                    testLeaderStatus.setApplicationFormId(applicationFormId);
                    testLeaderStatus.setRound(currentStatus.getRound());
                    testLeaderStatus.setStepOrder(CurrentApprovalStatus.STEP_TWO);
                    testLeaderStatus.setApproverRole("test_leader");
                    testLeaderStatus.setApproverUserName(form.getTestLeader());
                    testLeaderStatus.setStatus(CurrentApprovalStatus.STATUS_PEDING); // 待审批
                    nextApprovals.add(testLeaderStatus);

                    // 添加研发组长
                    CurrentApprovalStatus devLeaderStatus = new CurrentApprovalStatus();
                    devLeaderStatus.setApplicationFormId(applicationFormId);
                    devLeaderStatus.setRound(currentStatus.getRound());
                    devLeaderStatus.setStepOrder(CurrentApprovalStatus.STEP_TWO);
                    devLeaderStatus.setApproverRole("dev_leader");
                    devLeaderStatus.setApproverUserName(form.getDevLeader());
                    devLeaderStatus.setStatus(CurrentApprovalStatus.STATUS_PEDING); // 待审批
                    nextApprovals.add(devLeaderStatus);

                    // 保存第二阶段审批状态
                    currentApprovalStatusRepository.saveAll(nextApprovals);
                } else {
                    // 第二阶段也完成，整个审批流程结束
                    form.setStatus(DeviceApplicationForm.STATUS_APPROVED); // 审批通过状态
                    deviceApplicationFormRepository.save(form);

                    // 处理设备信息申请
                    processDeviceInfoApplication(applicationFormId);

                    // 开始自动处理流程（固件校验 -> 同步）
                    startAutoProcess(applicationFormId);

                    // 清除当前审批状态
                    currentApprovalStatusRepository.deleteByApplicationFormIdAndRound(
                            applicationFormId, currentStatus.getRound());
                }
            }
        }
    }




    // 添加自动处理流程启动方法
    private void startAutoProcess(Integer applicationFormId) {
        DeviceApplicationForm form = deviceApplicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new RuntimeException("申请单不存在"));

        // 只有新增、修改、上线、下线申请才需要自动处理
        if (form.getApplicationType() >= 0 && form.getApplicationType() <= 3) {
            form.setStatus(DeviceApplicationForm.STATUS_AUTO_PROCESSING);
            deviceApplicationFormRepository.save(form);

            // 异步执行自动处理流程
            completeProcessBasedOnType(form);
        } else {
            // 其他类型直接完成
            form.setStatus(DeviceApplicationForm.STATUS_COMPLETED);
            deviceApplicationFormRepository.save(form);
        }
    }


    @Override
    public List<ApprovalRecordDto> getApprovalHistory(Integer applicationFormId) {
        List<ApprovalRecord> records = approvalRecordRepository
                .findByApplicationFormIdOrderByRoundAscStepOrderAscApprovedAtAsc(applicationFormId);

        return records.stream().map(record -> {
            ApprovalRecordDto dto = new ApprovalRecordDto();
            dto.setId(record.getId());
            dto.setApplicationFormId(record.getApplicationFormId());
            dto.setRound(record.getRound());
            dto.setStepOrder(record.getStepOrder());
            dto.setApproverRole(record.getApproverRole());
            dto.setApproverUserName(record.getApproverUserName());
            dto.setApprovalStatus(record.getApprovalStatus());
            dto.setComment(record.getComment());
            dto.setApprovedAt(record.getApprovedAt());

            switch (record.getApprovalStatus()) {
                case 0: dto.setApprovalStatusText("待审批"); break;
                case 1: dto.setApprovalStatusText("通过"); break;
                case 2: dto.setApprovalStatusText("驳回"); break;
                default: dto.setApprovalStatusText("未知");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processDeviceInfoApplication(Integer applicationFormId) {
        DeviceApplicationForm form = deviceApplicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new RuntimeException("申请单不存在"));

        // 只有审批通过的申请单才能处理
        if (!form.getStatus().equals(DeviceApplicationForm.STATUS_APPROVED)) {
            throw new RuntimeException("申请单未审批通过");
        }

        switch (form.getApplicationType()) {
            case DeviceApplicationForm.APPLICATION_TYPE_ADD:    // 新增
                createNewDevice(form);
                break;
            case DeviceApplicationForm.APPLICATION_TYPE_MODIFY: // 修改
                updateDevice(form);
                break;
            case DeviceApplicationForm.APPLICATION_TYPE_ONLINE: // 上线
                setDeviceStatus(form, DeviceInfo.STATUS_ONLINE);
                break;
            case DeviceApplicationForm.APPLICATION_TYPE_OFFLINE: // 下线
                setDeviceStatus(form, DeviceInfo.STATUS_OFFLINE);
                break;
            default:
                throw new RuntimeException("不支持的申请类型");
        }
    }

    // 修改 DeviceApplicationFormServiceImpl.java 中的 createNewDevice 方法
    private void createNewDevice(DeviceApplicationForm form) {
        DeviceInfo deviceInfo = deviceInfoService.parseDeviceInfoDetails(form.getDeviceInfoDetails());
        deviceInfoService.createNewDevice(deviceInfo);

        form.setApplicationDataId(deviceInfo.getId());
        form.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        deviceApplicationFormRepository.save(form);
    }

    private void updateDevice(DeviceApplicationForm form) {
        DeviceInfo deviceInfo = deviceInfoService.parseDeviceInfoDetails(form.getDeviceInfoDetails());
        deviceInfo.setId(form.getApplicationDataId());
        deviceInfoService.updateDevice(deviceInfo);

        form.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        deviceApplicationFormRepository.save(form);
    }

    private void setDeviceStatus(DeviceApplicationForm form, Integer status) {
        deviceInfoService.setDeviceStatus(form.getApplicationDataId(), status);
    }



    /**
     * 通用设备数据解析方法
     * @param deviceDataDetails 设备数据JSON字符串
     * @param clazz 目标类类型
     * @param <T> 目标类型
     * @return 解析后的对象
     */
    private <T> T parseDeviceData(String deviceDataDetails, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(deviceDataDetails, clazz);
        } catch (Exception e) {
            throw new RuntimeException("解析设备数据失败: " + clazz.getSimpleName(), e);
        }
    }



    // 在 DeviceApplicationFormServiceImpl.java 中添加以下方法

        // 修改 DeviceApplicationFormServiceImpl.java 中的 saveDraft 方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDraft(DeviceApplicationFormVo vo) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        DeviceApplicationForm form;

        if (vo.getId() == null) {
            // 新建草稿
            form = new DeviceApplicationForm();
            form.setStatus(DeviceApplicationForm.STATUS_DRAFT); // 草稿状态
            form.setCreatedAt(now);
        } else {
            // 更新草稿
            form = deviceApplicationFormRepository.findById(vo.getId())
                    .orElseThrow(() -> new RuntimeException("申请单不存在"));

            // 只能更新草稿状态的申请单
            if (!form.getStatus().equals(DeviceApplicationForm.STATUS_DRAFT)) {
                throw new RuntimeException("只能更新草稿状态的申请单");
            }
        }
        form.setUpdatedAt(now);
        // 映射VO到Entity
        mapVoToEntity(form, vo);
        deviceApplicationFormRepository.save(form);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualTriggerFirmwareVerify(Integer applicationFormId) {
        DeviceApplicationForm form = deviceApplicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new RuntimeException("申请单不存在"));

        // 检查是否可以手动触发固件校验
        if (form.getStatus() != DeviceApplicationForm.STATUS_FIRMWARE_FAILED &&
                form.getStatus() != DeviceApplicationForm.STATUS_AUTO_FAILED) {
            throw new RuntimeException("当前状态不允许手动触发固件校验");
        }

        // 更新状态为手动触发
        form.setStatus(DeviceApplicationForm.STATUS_MANUAL_TRIGGERED);
        form.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        deviceApplicationFormRepository.save(form);

        // 异步执行固件校验
        executeFirmwareVerification(form);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualTriggerSync(Integer applicationFormId) {
        DeviceApplicationForm form = deviceApplicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new RuntimeException("申请单不存在"));

        // 检查是否可以手动触发同步
        if (form.getStatus() != DeviceApplicationForm.STATUS_SYNC_FAILED &&
                form.getStatus() != DeviceApplicationForm.STATUS_AUTO_FAILED) {
            throw new RuntimeException("当前状态不允许手动触发同步");
        }

        // 更新状态为手动触发
        form.setStatus(DeviceApplicationForm.STATUS_MANUAL_TRIGGERED);
        form.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        deviceApplicationFormRepository.save(form);

        // 异步执行同步
        executeSync(form);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualCompleteProcess(Integer applicationFormId) {
        DeviceApplicationForm form = deviceApplicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new RuntimeException("申请单不存在"));

        // 检查是否可以手动完成流程
        if (form.getStatus() != DeviceApplicationForm.STATUS_AUTO_FAILED) {
            throw new RuntimeException("当前状态不允许手动完成流程");
        }

        // 根据申请类型完成后续流程
        completeProcessBasedOnType(form);
    }

    // 固件校验预留方法
    private void executeFirmwareVerification(DeviceApplicationForm form) {
        // TODO: 实现固件校验逻辑
        // 这里可以调用实际的固件校验服务

        try {
            // 模拟固件校验过程
            // firmwareVerificationService.verify(form);

            // 校验成功后进入同步阶段
            form.setStatus(DeviceApplicationForm.STATUS_SYNCING);
            deviceApplicationFormRepository.save(form);

            // 异步执行同步
            executeSync(form);
        } catch (Exception e) {
            // 固件校验失败
            form.setStatus(DeviceApplicationForm.STATUS_FIRMWARE_FAILED);
            deviceApplicationFormRepository.save(form);
            // 记录错误日志
            // log.error("固件校验失败", e);
        }
    }

    // 同步预留方法
    private void executeSync(DeviceApplicationForm form) {
        // TODO: 实现同步逻辑
        // 这里可以调用实际的同步服务

        try {
            // 模拟同步过程
            // syncService.sync(form, form.getApplicationType() == 2 || form.getApplicationType() == 0); // 上线/新增为上线同步，下线为下线同步

            // 同步成功后完成流程
            form.setStatus(DeviceApplicationForm.STATUS_COMPLETED);
            deviceApplicationFormRepository.save(form);

            // 更新设备信息状态
            updateDeviceInfoStatus(form);
        } catch (Exception e) {
            // 同步失败
            form.setStatus(DeviceApplicationForm.STATUS_SYNC_FAILED);
            deviceApplicationFormRepository.save(form);
            // 记录错误日志
            // log.error("同步失败", e);
        }
    }

    // 根据申请类型完成流程
    // 根据申请类型完成流程
    private void completeProcessBasedOnType(DeviceApplicationForm form) {
        switch (form.getApplicationType()) {
            case 0: // 新增
            case 1: // 修改
                // 新增和修改需要固件校验和同步
                form.setStatus(DeviceApplicationForm.STATUS_FIRMWARE_VERIFY);
                deviceApplicationFormRepository.save(form);
                executeFirmwareVerification(form);
                break;
            case 2: // 上线
            case 3: // 下线
                // 上线和下线只需要同步
                form.setStatus(DeviceApplicationForm.STATUS_SYNCING);
                deviceApplicationFormRepository.save(form);
                executeSync(form);
                break;
            default:
                throw new RuntimeException("不支持的申请类型");
        }
    }


    // 更新设备信息状态
    // 修改 DeviceApplicationFormServiceImpl.java 中的 updateDeviceInfoStatus 方法
    private void updateDeviceInfoStatus(DeviceApplicationForm form) {
        if (form.getApplicationType().equals(DeviceApplicationForm.APPLICATION_TYPE_ONLINE)) { // 上线申请
            setDeviceStatus(form, DeviceInfo.STATUS_ONLINE); // 设置为上线
        } else if (form.getApplicationType().equals(DeviceApplicationForm.APPLICATION_TYPE_OFFLINE)) { // 下线申请
            setDeviceStatus(form, DeviceInfo.STATUS_OFFLINE); // 设置为下线
        }
        // 新增和修改申请默认已经是上线状态
    }

    // 在 DeviceApplicationFormServiceImpl.java 中替换之前的 deleteApplicationForm 方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteApplicationForm(Integer applicationFormId, String applicantName) {
        DeviceApplicationForm form = deviceApplicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new RuntimeException("申请单不存在"));

        // 检查申请人是否匹配
        if (!form.getApplicantUserName().equals(applicantName)) {
            throw new RuntimeException("只有申请人才能删除申请单");
        }

        // 检查当前状态是否允许删除（只有草稿状态才能删除）
        if (!form.getStatus().equals(DeviceApplicationForm.STATUS_DRAFT)) {
            throw new RuntimeException("只有草稿状态的申请单才能删除");
        }

        // 删除相关的审批记录
        try {
            approvalRecordRepository.deleteByApplicationFormId(applicationFormId);
        } catch (Exception e) {
            // 如果没有相关审批记录，忽略异常
        }

        // 删除相关的当前审批状态
        try {
            currentApprovalStatusRepository.deleteByApplicationFormId(applicationFormId);
        } catch (Exception e) {
            // 如果没有相关审批状态，忽略异常
        }

        // 删除申请单本身
        deviceApplicationFormRepository.deleteById(applicationFormId);
    }
}