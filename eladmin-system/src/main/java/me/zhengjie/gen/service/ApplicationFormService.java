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

import me.zhengjie.gen.domain.ApplicationForm;
import me.zhengjie.gen.domain.vo.ApplicationFormVo;
import me.zhengjie.gen.service.dto.ApprovalRecordDto;
import me.zhengjie.gen.service.dto.ApplicationFormDto;
import me.zhengjie.gen.service.dto.ApplicationFormQueryCriteria;
import me.zhengjie.gen.service.dto.PendingApprovalDto;
import org.springframework.data.domain.Pageable;

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
public interface ApplicationFormService {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    PageResult<ApplicationFormDto> queryAll(ApplicationFormQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<ApplicationFormDto>
     */
    List<ApplicationFormDto> queryAll(ApplicationFormQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ApplicationFormDto
     */
    ApplicationFormDto findById(Integer id);

    /**
     * 创建
     * @param resources /
     */
    void create(ApplicationForm resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(ApplicationForm resources);

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
    void download(List<ApplicationFormDto> all, HttpServletResponse response) throws IOException;

    /**
     * 提交申请单（包括首次提交和重新提交）
     * @param resources 申请单
     */
    void submitApplication(ApplicationFormVo resources);

    /**
     * 查询当前用户的待审批任务
     * @param approverUserName 审批人姓名
     * @return 待审批任务列表
     */
    List<PendingApprovalDto> getPendingApprovals(String approverUserName);


    // 修改 ApplicationFormService.java 中的方法签名
    PageResult<ApplicationFormDto> getApprovedApplications(ApplicationFormQueryCriteria criteria, Pageable pageable);


    /**
     * 审批申请单
     * @param applicationFormId 申请单ID
     * @param approverUserName 审批人姓名
     * @param approvalStatus 审批状态（1-通过，2-驳回）
     * @param comment 审批意见
     */
    void approveApplication(Integer applicationFormId, String approverUserName, Integer approvalStatus, String comment);

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
    void ApplicationPostProcess(Integer applicationFormId);

    /**
     * 保存申请单草稿
     * @param resources 申请单
     */
    void saveDraft(ApplicationFormVo resources);

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


    PageResult<PendingApprovalDto> getPendingApprovals(ApplicationFormQueryCriteria criteria, Pageable pageable);

    /**
     * 撤回申请单
     * @param applicationFormId 申请单ID
     * @param applicantName 申请人姓名
     */
    void withdrawApplication(Integer applicationFormId, String applicantName);

    // 在 ApplicationFormService.java 中添加以下方法
    /**
     * 删除申请单（仅草稿状态可删除）
     * @param applicationFormId 申请单ID
     * @param applicantName 申请人姓名
     */
    void deleteApplicationForm(Integer applicationFormId, String applicantName);

}