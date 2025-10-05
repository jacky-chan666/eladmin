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
package me.zhengjie.gen.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.gen.domain.ApplicationForm;
import me.zhengjie.gen.domain.vo.ApplicationFormVo;
import me.zhengjie.gen.service.ApplicationFormService;
import me.zhengjie.gen.service.dto.ApplicationFormQueryCriteria;
import me.zhengjie.gen.service.dto.PendingApprovalDto;
import me.zhengjie.gen.service.dto.ApprovalRecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.gen.service.dto.ApplicationFormDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @website https://eladmin.vip
 * @author Chen Jiayuan
 * @date 2025-09-18
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "审核接口")
@RequestMapping("/api/applicationForm")
public class ApplicationFormController {

    @Autowired
    private final ApplicationFormService applicationFormService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('applicationForm:list')")
    public void exportApplicationForm(HttpServletResponse response, ApplicationFormQueryCriteria criteria) throws IOException {
        applicationFormService.download(applicationFormService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询审核接口")
    @PreAuthorize("@el.check('applicationForm:list')")
    public ResponseEntity<PageResult<ApplicationFormDto>> queryApplicationForm(ApplicationFormQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(applicationFormService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping("/delete")
    @Log("删除申请单")
    @ApiOperation("删除申请单")
    @PreAuthorize("@el.check('applicationForm:list')")
    public ResponseEntity<Object> deleteApplicationForm(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId,
            @ApiParam(value = "申请人姓名") @RequestParam String applicantUserName) {
        applicationFormService.deleteApplicationForm(applicationFormId, applicantUserName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/submit")
    @Log("提交申请单")
    @ApiOperation("提交申请单（包括首次提交和重新提交）")
    @PreAuthorize("@el.check('applicationForm:list')")
    public ResponseEntity<Object> submitApplication(@Validated @RequestBody ApplicationFormVo resources){
        applicationFormService.submitApplication(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/pending-approvals")
    @ApiOperation("查询当前用户的待审批任务（分页）")
    @PreAuthorize("@el.check('applicationForm:list')")
    public ResponseEntity<PageResult<PendingApprovalDto>> queryPendingApprovals(
            ApplicationFormQueryCriteria criteria,
            Pageable pageable) {
        PageResult<PendingApprovalDto> result = applicationFormService.getPendingApprovals(criteria, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/approved-applications")
    @ApiOperation("查询当前用户已审批的任务（分页）")
    @PreAuthorize("@el.check('applicationForm:list')")
    public ResponseEntity<PageResult<ApplicationFormDto>> getApprovedApplications(
            ApplicationFormQueryCriteria criteria,
            Pageable pageable) {
        PageResult<ApplicationFormDto> result = applicationFormService.getApprovedApplications(criteria, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/approve")
    @Log("审批申请单")
    @ApiOperation("审批申请单")
    @PreAuthorize("@el.check('applicationForm:list')")
    public ResponseEntity<Object> approveApplication(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId,
            @ApiParam(value = "审批人姓名") @RequestParam String approverUserName,
            @ApiParam(value = "审批状态：1-通过，2-驳回") @RequestParam Integer approvalStatus,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String comment){
        applicationFormService.approveApplication(applicationFormId, approverUserName, approvalStatus, comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/approval-history/{applicationFormId}")
    @ApiOperation("查询申请单的所有审批历史")
    @PreAuthorize("@el.check('applicationForm:list')")
    public ResponseEntity<List<ApprovalRecordDto>> getApprovalHistory(
            @ApiParam(value = "申请单ID") @PathVariable Integer applicationFormId){
        return new ResponseEntity<>(applicationFormService.getApprovalHistory(applicationFormId), HttpStatus.OK);
    }

    @PostMapping("/save-draft")
    @Log("保存申请单草稿")
    @ApiOperation("保存申请单草稿")
    @PreAuthorize("@el.check('applicationForm:list') or @el.check('applicationForm:list')")
    public ResponseEntity<Object> saveDraft(@Validated @RequestBody ApplicationFormVo resources){
        applicationFormService.saveDraft(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    @Log("撤回申请单")
    @ApiOperation("撤回申请单")
    @PreAuthorize("@el.check('applicationForm:list')")
    public ResponseEntity<Object> withdrawApplication(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId,
            @ApiParam(value = "申请人姓名") @RequestParam String applicantUserName) {
        applicationFormService.withdrawApplication(applicationFormId, applicantUserName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/manual-trigger-firmware")
    @Log("手动触发固件校验")
    @ApiOperation("手动触发固件校验")
    @PreAuthorize("@el.check('applicationForm:manual')")
    public ResponseEntity<Object> manualTriggerFirmwareVerify(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId){
        applicationFormService.manualTriggerFirmwareVerify(applicationFormId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/manual-trigger-sync")
    @Log("手动触发同步")
    @ApiOperation("手动触发同步")
    @PreAuthorize("@el.check('applicationForm:manual')")
    public ResponseEntity<Object> manualTriggerSync(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId){
        applicationFormService.manualTriggerSync(applicationFormId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}