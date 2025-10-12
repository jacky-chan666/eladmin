/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.rest;

import com.tplink.smb.common.data.management.system.annotation.Log;
import com.tplink.smb.common.data.management.system.gen.service.ApplicationFormService;
import com.tplink.smb.common.data.management.system.gen.service.dto.ApplicationFormDto;
import com.tplink.smb.common.data.management.system.gen.service.dto.ApplicationFormQueryCriteria;
import com.tplink.smb.common.data.management.system.gen.service.dto.ApprovalRecordDto;
import com.tplink.smb.common.data.management.system.gen.service.dto.PendingApprovalDto;
import com.tplink.smb.common.data.management.system.gen.domain.vo.ApplicationFormVo;
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
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.tplink.smb.common.data.management.system.utils.PageResult;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "审核接口")
@RequestMapping("/api/applicationForm")
public class ApplicationFormController {

  @Autowired private final ApplicationFormService applicationFormService;

  @ApiOperation("导出数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('applicationForm:list')")
  public void exportApplicationForm(
      HttpServletResponse response, ApplicationFormQueryCriteria criteria) throws IOException {
    applicationFormService.download(applicationFormService.queryAll(criteria), response);
  }

  @GetMapping
  @ApiOperation("查询审核接口")
  @PreAuthorize("@el.check('applicationForm:list')")
  public ResponseEntity<PageResult<ApplicationFormDto>> queryApplicationForm(
      ApplicationFormQueryCriteria criteria, Pageable pageable) {
    return new ResponseEntity<>(applicationFormService.queryAll(criteria, pageable), HttpStatus.OK);
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
  public ResponseEntity<Object> submitApplication(
      @Validated @RequestBody ApplicationFormVo resources) {
    applicationFormService.submitApplication(resources);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/pending-approvals")
  @ApiOperation("查询当前用户的待审批任务（分页）")
  @PreAuthorize("@el.check('applicationForm:list')")
  public ResponseEntity<PageResult<PendingApprovalDto>> queryPendingApprovals(
      ApplicationFormQueryCriteria criteria, Pageable pageable) {
    PageResult<PendingApprovalDto> result =
        applicationFormService.getPendingApprovals(criteria, pageable);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/approved-applications")
  @ApiOperation("查询当前用户已审批的任务（分页）")
  @PreAuthorize("@el.check('applicationForm:list')")
  public ResponseEntity<PageResult<ApplicationFormDto>> getApprovedApplications(
      ApplicationFormQueryCriteria criteria, Pageable pageable) {
    PageResult<ApplicationFormDto> result =
        applicationFormService.getApprovedApplications(criteria, pageable);
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
      @ApiParam(value = "审批意见") @RequestParam(required = false) String comment) {
    applicationFormService.approveApplication(
        applicationFormId, approverUserName, approvalStatus, comment);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/approval-history/{applicationFormId}")
  @ApiOperation("查询申请单的所有审批历史")
  @PreAuthorize("@el.check('applicationForm:list')")
  public ResponseEntity<List<ApprovalRecordDto>> getApprovalHistory(
      @ApiParam(value = "申请单ID") @PathVariable Integer applicationFormId) {
    return new ResponseEntity<>(
        applicationFormService.getApprovalHistory(applicationFormId), HttpStatus.OK);
  }

  @PostMapping("/save-draft")
  @Log("保存申请单草稿")
  @ApiOperation("保存申请单草稿")
  @PreAuthorize("@el.check('applicationForm:list') or @el.check('applicationForm:list')")
  public ResponseEntity<Object> saveDraft(@Validated @RequestBody ApplicationFormVo resources) {
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
      @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId) {
    applicationFormService.manualTriggerFirmwareVerify(applicationFormId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/manual-trigger-sync")
  @Log("手动触发同步")
  @ApiOperation("手动触发同步")
  @PreAuthorize("@el.check('applicationForm:manual')")
  public ResponseEntity<Object> manualTriggerSync(
      @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId) {
    applicationFormService.manualTriggerSync(applicationFormId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
