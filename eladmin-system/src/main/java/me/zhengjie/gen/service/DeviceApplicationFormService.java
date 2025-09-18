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
package me.zhengjie.gen.service;

import me.zhengjie.gen.domain.DeviceApplicationForm;
import me.zhengjie.gen.service.dto.ApprovalRecordDto;
import me.zhengjie.gen.service.dto.DeviceApplicationFormDto;
import me.zhengjie.gen.service.dto.DeviceApplicationFormQueryCriteria;
import me.zhengjie.gen.service.dto.PendingApprovalDto;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author Chen Jiayuan
* @date 2025-09-18
**/
public interface DeviceApplicationFormService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<DeviceApplicationFormDto> queryAll(DeviceApplicationFormQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DeviceApplicationFormDto>
    */
    List<DeviceApplicationFormDto> queryAll(DeviceApplicationFormQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return DeviceApplicationFormDto
     */
    DeviceApplicationFormDto findById(Integer id);

    /**
    * 创建
    * @param resources /
    */
    void create(DeviceApplicationForm resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(DeviceApplicationForm resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Integer[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<DeviceApplicationFormDto> all, HttpServletResponse response) throws IOException;

    /**
     * 提交申请单（包括首次提交和重新提交）
     * @param resources 申请单
     */
    void submitApplication(DeviceApplicationForm resources);

    /**
     * 查询当前用户的待审批任务
     * @param approverName 审批人姓名
     * @return 待审批任务列表
     */
    List<PendingApprovalDto> getPendingApprovals(String approverName);

    /**
     * 查询当前用户已审批的任务
     * @param approverName 审批人姓名
     * @return 已审批任务列表
     */
    List<DeviceApplicationFormDto> getApprovedApplications(String approverName);

    /**
     * 审批申请单
     * @param applicationFormId 申请单ID
     * @param approverName 审批人姓名
     * @param approvalStatus 审批状态（1-通过，2-驳回）
     * @param comment 审批意见
     */
    void approveApplication(Integer applicationFormId, String approverName, Integer approvalStatus, String comment);

    /**
     * 查询申请单的所有审批历史
     * @param applicationFormId 申请单ID
     * @return 审批历史列表
     */
    List<ApprovalRecordDto> getApprovalHistory(Integer applicationFormId);

    /**
     * 处理设备信息申请（新增、修改、上线、下线）
     * @param applicationFormId 申请单ID
     */
    void processDeviceInfoApplication(Integer applicationFormId);

    /**
     * 保存申请单草稿
     * @param resources 申请单
     */
    void saveDraft(DeviceApplicationForm resources);

    /**
     * 手动触发固件校验
     * @param applicationFormId 申请单ID
     */
    void manualTriggerFirmwareVerify(Integer applicationFormId);

    /**
     * 手动触发同步
     * @param applicationFormId 申请单ID
     */
    void manualTriggerSync(Integer applicationFormId);

    /**
     * 手动完成申请单流程
     * @param applicationFormId 申请单ID
     */
    void manualCompleteProcess(Integer applicationFormId);
}