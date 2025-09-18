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

import me.zhengjie.gen.domain.ApprovalRecord;
import me.zhengjie.gen.domain.CurrentApprovalStatus;
import me.zhengjie.gen.domain.DeviceApplicationForm;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.gen.domain.DeviceInfo;
import me.zhengjie.gen.repository.ApprovalRecordRepository;
import me.zhengjie.gen.repository.CurrentApprovalStatusRepository;
import me.zhengjie.gen.repository.DeviceInfoRepository;
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

    @Override
    public PageResult<DeviceApplicationFormDto> queryAll(DeviceApplicationFormQueryCriteria criteria, Pageable pageable){
        Page<DeviceApplicationForm> page = deviceApplicationFormRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(deviceApplicationFormMapper::toDto));
    }

    @Override
    public List<DeviceApplicationFormDto> queryAll(DeviceApplicationFormQueryCriteria criteria){
        return deviceApplicationFormMapper.toDto(deviceApplicationFormRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DeviceApplicationFormDto findById(Integer id) {
        DeviceApplicationForm deviceApplicationForm = deviceApplicationFormRepository.findById(id).orElseGet(DeviceApplicationForm::new);
        ValidationUtil.isNull(deviceApplicationForm.getId(),"DeviceApplicationForm","id",id);
        return deviceApplicationFormMapper.toDto(deviceApplicationForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DeviceApplicationForm resources) {
        if(deviceApplicationFormRepository.findByApplicantId(resources.getApplicantId()) != null){
            throw new EntityExistException(DeviceApplicationForm.class,"applicant_id",resources.getApplicantId());
        }
        if(deviceApplicationFormRepository.findByApplicationDataId(resources.getApplicationDataId()) != null){
            throw new EntityExistException(DeviceApplicationForm.class,"application_data_id",resources.getApplicationDataId().toString());
        }
        deviceApplicationFormRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceApplicationForm resources) {
        DeviceApplicationForm deviceApplicationForm = deviceApplicationFormRepository.findById(resources.getId()).orElseGet(DeviceApplicationForm::new);
        ValidationUtil.isNull( deviceApplicationForm.getId(),"DeviceApplicationForm","id",resources.getId());
        DeviceApplicationForm deviceApplicationForm1 = null;
        deviceApplicationForm1 = deviceApplicationFormRepository.findByApplicantId(resources.getApplicantId());
        if(deviceApplicationForm1 != null && !deviceApplicationForm1.getId().equals(deviceApplicationForm.getId())){
            throw new EntityExistException(DeviceApplicationForm.class,"applicant_id",resources.getApplicantId());
        }
        deviceApplicationForm1 = deviceApplicationFormRepository.findByApplicationDataId(resources.getApplicationDataId());
        if(deviceApplicationForm1 != null && !deviceApplicationForm1.getId().equals(deviceApplicationForm.getId())){
            throw new EntityExistException(DeviceApplicationForm.class,"application_data_id",resources.getApplicationDataId().toString());
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
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("申请单UUID", deviceApplicationForm.getApplicantId());
            map.put("申请人姓名", deviceApplicationForm.getApplicantName());
            map.put("所属部门", deviceApplicationForm.getDepartment());
            map.put("申请日期", deviceApplicationForm.getApplicationDate());
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
            map.put(" createdAt",  deviceApplicationForm.getCreatedAt());
            map.put(" updatedAt",  deviceApplicationForm.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitApplication(DeviceApplicationForm resources) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // 保存或更新申请单
        if (resources.getId() == null) {
            // 首次提交
            resources.setStatus(0); // 待审批状态
            resources.setCreatedAt(now);
            resources.setUpdatedAt(now);
            deviceApplicationFormRepository.save(resources);
        } else {
            // 重新提交
            DeviceApplicationForm existing = deviceApplicationFormRepository.findById(resources.getId())
                    .orElseThrow(() -> new RuntimeException("申请单不存在"));
            existing.setApplicationTitle(resources.getApplicationTitle());
            existing.setApplicationReason(resources.getApplicationReason());
            existing.setTestContact(resources.getTestContact());
            existing.setTestLeader(resources.getTestLeader());
            existing.setDevContact(resources.getDevContact());
            existing.setDevLeader(resources.getDevLeader());
            existing.setStatus(0); // 重置为待审批状态
            existing.setUpdatedAt(now);
            deviceApplicationFormRepository.save(existing);
        }

        // 获取申请单ID
        Integer applicationFormId = resources.getId() != null ? resources.getId() : resources.getId();

        // 确定审批轮次
        Integer round = approvalRecordRepository.findMaxRoundByApplicationFormId(applicationFormId) + 1;

        // 清除之前的审批状态
        currentApprovalStatusRepository.deleteByApplicationFormIdAndRound(applicationFormId, round - 1);

        // 初始化第一阶段审批状态（并行审批）
        List<CurrentApprovalStatus> initialApprovals = new ArrayList<>();

        // 添加测试接口人
        CurrentApprovalStatus testContactStatus = new CurrentApprovalStatus();
        testContactStatus.setApplicationFormId(applicationFormId);
        testContactStatus.setRound(round);
        testContactStatus.setStepOrder(1);
        testContactStatus.setApproverRole("test_contact");
        testContactStatus.setApproverName(resources.getTestContact());
        testContactStatus.setStatus(0); // 待审批
        initialApprovals.add(testContactStatus);

        // 添加研发接口人
        CurrentApprovalStatus devContactStatus = new CurrentApprovalStatus();
        devContactStatus.setApplicationFormId(applicationFormId);
        devContactStatus.setRound(round);
        devContactStatus.setStepOrder(1);
        devContactStatus.setApproverRole("dev_contact");
        devContactStatus.setApproverName(resources.getDevContact());
        devContactStatus.setStatus(0); // 待审批
        initialApprovals.add(devContactStatus);

        // 保存初始审批状态
        currentApprovalStatusRepository.saveAll(initialApprovals);
    }

    @Override
    public List<PendingApprovalDto> getPendingApprovals(String approverName) {
        List<CurrentApprovalStatus> pendingStatuses = currentApprovalStatusRepository
                .findByApproverNameAndStatusOrderByApplicationFormId(approverName, 0);

        List<PendingApprovalDto> result = new ArrayList<>();
        for (CurrentApprovalStatus status : pendingStatuses) {
            Optional<DeviceApplicationForm> formOpt = deviceApplicationFormRepository
                    .findById(status.getApplicationFormId());

            if (formOpt.isPresent() && (formOpt.get().getStatus() == 0 || formOpt.get().getStatus() == 1)) {
                PendingApprovalDto dto = new PendingApprovalDto();
                dto.setApplicationForm(deviceApplicationFormMapper.toDto(formOpt.get()));
                dto.setRound(status.getRound());
                dto.setStepOrder(status.getStepOrder());
                result.add(dto);
            }
        }

        return result;
    }

    @Override
    public List<DeviceApplicationFormDto> getApprovedApplications(String approverName) {
        List<ApprovalRecord> records = approvalRecordRepository
                .findByApproverNameAndApprovalStatusInOrderByApprovedAtDesc(
                        approverName, Arrays.asList(1, 2));

        // 去重，只保留每个申请单的最新审批记录
        Map<Integer, ApprovalRecord> latestApprovals = new LinkedHashMap<>();
        for (ApprovalRecord record : records) {
            if (!latestApprovals.containsKey(record.getApplicationFormId()) ||
                    record.getApprovedAt().after(latestApprovals.get(record.getApplicationFormId()).getApprovedAt())) {
                latestApprovals.put(record.getApplicationFormId(), record);
            }
        }

        List<DeviceApplicationFormDto> result = new ArrayList<>();
        for (ApprovalRecord record : latestApprovals.values()) {
            Optional<DeviceApplicationForm> formOpt = deviceApplicationFormRepository
                    .findById(record.getApplicationFormId());
            if (formOpt.isPresent()) {
                result.add(deviceApplicationFormMapper.toDto(formOpt.get()));
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveApplication(Integer applicationFormId, String approverName,
                                   Integer approvalStatus, String comment) {
        Optional<DeviceApplicationForm> formOpt = deviceApplicationFormRepository.findById(applicationFormId);
        if (!formOpt.isPresent()) {
            throw new RuntimeException("申请单不存在");
        }

        DeviceApplicationForm form = formOpt.get();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // 查找当前审批状态
        List<CurrentApprovalStatus> currentStatuses = currentApprovalStatusRepository
                .findByApplicationFormIdAndRound(applicationFormId,
                        approvalRecordRepository.findMaxRoundByApplicationFormId(applicationFormId));

        CurrentApprovalStatus currentStatus = currentStatuses.stream()
                .filter(s -> s.getApproverName().equals(approverName) && s.getStatus() == 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("当前用户没有待审批的任务"));

        // 创建审批记录
        ApprovalRecord record = new ApprovalRecord();
        record.setApplicationFormId(applicationFormId);
        record.setRound(currentStatus.getRound());
        record.setStepOrder(currentStatus.getStepOrder());
        record.setApproverRole(currentStatus.getApproverRole());
        record.setApproverName(approverName);
        record.setApprovalStatus(approvalStatus);
        record.setComment(comment);
        record.setApprovedAt(now);
        approvalRecordRepository.save(record);

        // 更新当前审批状态
        currentStatus.setStatus(1); // 已审批
        currentApprovalStatusRepository.save(currentStatus);

        if (approvalStatus == 2) {
            // 审批被驳回，更新申请单状态
            form.setStatus(2); // 被驳回状态
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
                if (currentStatus.getStepOrder() == 1) {
                    // 进入第二阶段审批
                    List<CurrentApprovalStatus> nextApprovals = new ArrayList<>();

                    // 添加测试组长
                    CurrentApprovalStatus testLeaderStatus = new CurrentApprovalStatus();
                    testLeaderStatus.setApplicationFormId(applicationFormId);
                    testLeaderStatus.setRound(currentStatus.getRound());
                    testLeaderStatus.setStepOrder(2);
                    testLeaderStatus.setApproverRole("test_leader");
                    testLeaderStatus.setApproverName(form.getTestLeader());
                    testLeaderStatus.setStatus(0); // 待审批
                    nextApprovals.add(testLeaderStatus);

                    // 添加研发组长
                    CurrentApprovalStatus devLeaderStatus = new CurrentApprovalStatus();
                    devLeaderStatus.setApplicationFormId(applicationFormId);
                    devLeaderStatus.setRound(currentStatus.getRound());
                    devLeaderStatus.setStepOrder(2);
                    devLeaderStatus.setApproverRole("dev_leader");
                    devLeaderStatus.setApproverName(form.getDevLeader());
                    devLeaderStatus.setStatus(0); // 待审批
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
            dto.setApproverName(record.getApproverName());
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

    // 在 DeviceApplicationFormServiceImpl.java 中添加以下方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processDeviceInfoApplication(Integer applicationFormId) {
        DeviceApplicationForm form = deviceApplicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new RuntimeException("申请单不存在"));

        // 只有审批通过的申请单才能处理
        if (form.getStatus() != 1) {
            throw new RuntimeException("申请单未审批通过");
        }

        switch (form.getApplicationType()) {
            case 0: // 新增
                createNewDevice(form);
                break;
            case 1: // 修改
                updateDevice(form);
                break;
            case 2: // 上线
                setDeviceStatus(form, 1);
                break;
            case 3: // 下线
                setDeviceStatus(form, 0);
                break;
            default:
                throw new RuntimeException("不支持的申请类型");
        }
    }

    private void createNewDevice(DeviceApplicationForm form) {
        // 解析设备信息详情
        DeviceInfo deviceInfo = parseDeviceInfoDetails(form.getDeviceInfoDetails());

        // 检查设备型号是否已存在
        if (deviceInfoRepository.existsByModel(deviceInfo.getModel())) {
            throw new RuntimeException("设备型号已存在");
        }

        // 设置默认状态为上线
        deviceInfo.setStatus(1);

        // 保存设备信息
        deviceInfo = deviceInfoRepository.save(deviceInfo);

        // 更新申请单的application_data_id
        form.setApplicationDataId(deviceInfo.getId());
        deviceApplicationFormRepository.save(form);
    }

    private void updateDevice(DeviceApplicationForm form) {
        DeviceInfo existingDevice = deviceInfoRepository.findById(form.getApplicationDataId())
                .orElseThrow(() -> new RuntimeException("设备不存在"));

        // 解析设备信息详情
        DeviceInfo updatedDevice = parseDeviceInfoDetails(form.getDeviceInfoDetails());

        // 更新设备信息
        existingDevice.setName(updatedDevice.getName());
        existingDevice.setType(updatedDevice.getType());
        existingDevice.setManufacturer(updatedDevice.getManufacturer());
        existingDevice.setSpecifications(updatedDevice.getSpecifications());
        existingDevice.setModelVersion(updatedDevice.getModelVersion());

        deviceInfoRepository.save(existingDevice);
    }

    private void setDeviceStatus(DeviceApplicationForm form, Integer status) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(form.getApplicationDataId())
                .orElseThrow(() -> new RuntimeException("设备不存在"));

        deviceInfo.setStatus(status);
        deviceInfoRepository.save(deviceInfo);
    }

    private DeviceInfo parseDeviceInfoDetails(String deviceInfoDetails) {
        // 这里需要实现JSON解析逻辑，根据实际使用的JSON库进行调整
        // 示例使用简单的手动解析
        DeviceInfo deviceInfo = new DeviceInfo();

        // 从JSON字符串中提取设备信息字段
        // 这里只是一个示例，实际需要根据JSON格式进行解析
        // 可以使用Jackson、Gson等JSON库

        return deviceInfo;
    }

    // 在 DeviceApplicationFormServiceImpl.java 中添加以下方法

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDraft(DeviceApplicationForm resources) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        if (resources.getId() == null) {
            // 新建草稿
            resources.setStatus(DeviceApplicationForm.STATUS_DRAFT); // 草稿状态
            resources.setCreatedAt(now);
            resources.setUpdatedAt(now);
            deviceApplicationFormRepository.save(resources);
        } else {
            // 更新草稿
            DeviceApplicationForm existing = deviceApplicationFormRepository.findById(resources.getId())
                    .orElseThrow(() -> new RuntimeException("申请单不存在"));

            // 只能更新草稿状态的申请单
            if (existing.getStatus() != DeviceApplicationForm.STATUS_DRAFT) {
                throw new RuntimeException("只能更新草稿状态的申请单");
            }

            existing.setApplicantId(resources.getApplicantId());
            existing.setApplicantName(resources.getApplicantName());
            existing.setDepartment(resources.getDepartment());
            existing.setApplicationDate(resources.getApplicationDate());
            existing.setApplicationType(resources.getApplicationType());
            existing.setApplicationDataType(resources.getApplicationDataType());
            existing.setApplicationTitle(resources.getApplicationTitle());
            existing.setApplicationReason(resources.getApplicationReason());
            existing.setTestContact(resources.getTestContact());
            existing.setTestLeader(resources.getTestLeader());
            existing.setDevContact(resources.getDevContact());
            existing.setDevLeader(resources.getDevLeader());
            existing.setDeviceInfoDetails(resources.getDeviceInfoDetails());
            existing.setUpdatedAt(now);

            deviceApplicationFormRepository.save(existing);
        }
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
    private void updateDeviceInfoStatus(DeviceApplicationForm form) {
        if (form.getApplicationType() == 2) { // 上线申请
            setDeviceStatus(form, 1); // 设置为上线
        } else if (form.getApplicationType() == 3) { // 下线申请
            setDeviceStatus(form, 0); // 设置为下线
        }
        // 新增和修改申请默认已经是上线状态
    }

}