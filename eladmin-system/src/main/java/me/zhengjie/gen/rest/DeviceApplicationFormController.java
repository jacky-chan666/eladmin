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
import me.zhengjie.gen.domain.DeviceApplicationForm;
import me.zhengjie.gen.service.DeviceApplicationFormService;
import me.zhengjie.gen.service.dto.DeviceApplicationFormQueryCriteria;
import me.zhengjie.gen.service.dto.PendingApprovalDto;
import me.zhengjie.gen.service.dto.ApprovalRecordDto;
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
import me.zhengjie.utils.PageResult;
import me.zhengjie.gen.service.dto.DeviceApplicationFormDto;
import me.zhengjie.gen.service.dto.DeviceApplicationFormDto;

/**
* @website https://eladmin.vip
* @author Chen Jiayuan
* @date 2025-09-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "审核接口")
@RequestMapping("/api/deviceApplicationForm")
public class DeviceApplicationFormController {

    private final DeviceApplicationFormService deviceApplicationFormService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deviceApplicationForm:list')")
    public void exportDeviceApplicationForm(HttpServletResponse response, DeviceApplicationFormQueryCriteria criteria) throws IOException {
        deviceApplicationFormService.download(deviceApplicationFormService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询审核接口")
    @PreAuthorize("@el.check('deviceApplicationForm:list')")
    public ResponseEntity<PageResult<DeviceApplicationFormDto>> queryDeviceApplicationForm(DeviceApplicationFormQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(deviceApplicationFormService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增审核接口")
    @ApiOperation("新增审核接口")
    @PreAuthorize("@el.check('deviceApplicationForm:add')")
    public ResponseEntity<Object> createDeviceApplicationForm(@Validated @RequestBody DeviceApplicationForm resources){
        deviceApplicationFormService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改审核接口")
    @ApiOperation("修改审核接口")
    @PreAuthorize("@el.check('deviceApplicationForm:edit')")
    public ResponseEntity<Object> updateDeviceApplicationForm(@Validated @RequestBody DeviceApplicationForm resources){
        deviceApplicationFormService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除审核接口")
    @ApiOperation("删除审核接口")
    @PreAuthorize("@el.check('deviceApplicationForm:del')")
    public ResponseEntity<Object> deleteDeviceApplicationForm(@ApiParam(value = "传ID数组[]") @RequestBody Integer[] ids) {
        deviceApplicationFormService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/submit")
    @Log("提交申请单")
    @ApiOperation("提交申请单（包括首次提交和重新提交）")
    @PreAuthorize("@el.check('deviceApplicationForm:add') or @el.check('deviceApplicationForm:edit')")
    public ResponseEntity<Object> submitApplication(@Validated @RequestBody DeviceApplicationForm resources){
        deviceApplicationFormService.submitApplication(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/pending-approvals")
    @ApiOperation("查询当前用户的待审批任务")
    @PreAuthorize("@el.check('deviceApplicationForm:list')")
    public ResponseEntity<List<PendingApprovalDto>> getPendingApprovals(
            @ApiParam(value = "审批人姓名") @RequestParam String approverName){
        return new ResponseEntity<>(deviceApplicationFormService.getPendingApprovals(approverName), HttpStatus.OK);
    }

    @GetMapping("/approved-applications")
    @ApiOperation("查询当前用户已审批的任务")
    @PreAuthorize("@el.check('deviceApplicationForm:list')")
    public ResponseEntity<List<DeviceApplicationFormDto>> getApprovedApplications(
            @ApiParam(value = "审批人姓名") @RequestParam String approverName){
        return new ResponseEntity<>(deviceApplicationFormService.getApprovedApplications(approverName), HttpStatus.OK);
    }

    @PostMapping("/approve")
    @Log("审批申请单")
    @ApiOperation("审批申请单")
    @PreAuthorize("@el.check('deviceApplicationForm:approve')")
    public ResponseEntity<Object> approveApplication(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId,
            @ApiParam(value = "审批人姓名") @RequestParam String approverName,
            @ApiParam(value = "审批状态：1-通过，2-驳回") @RequestParam Integer approvalStatus,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String comment){
        deviceApplicationFormService.approveApplication(applicationFormId, approverName, approvalStatus, comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/approval-history/{applicationFormId}")
    @ApiOperation("查询申请单的所有审批历史")
    @PreAuthorize("@el.check('deviceApplicationForm:list')")
    public ResponseEntity<List<ApprovalRecordDto>> getApprovalHistory(
            @ApiParam(value = "申请单ID") @PathVariable Integer applicationFormId){
        return new ResponseEntity<>(deviceApplicationFormService.getApprovalHistory(applicationFormId), HttpStatus.OK);
    }

    // 在 DeviceApplicationFormController.java 中添加以下方法

    @PostMapping("/save-draft")
    @Log("保存申请单草稿")
    @ApiOperation("保存申请单草稿")
    @PreAuthorize("@el.check('deviceApplicationForm:add') or @el.check('deviceApplicationForm:edit')")
    public ResponseEntity<Object> saveDraft(@Validated @RequestBody DeviceApplicationForm resources){
        deviceApplicationFormService.saveDraft(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/manual-trigger-firmware")
    @Log("手动触发固件校验")
    @ApiOperation("手动触发固件校验")
    @PreAuthorize("@el.check('deviceApplicationForm:manual')")
    public ResponseEntity<Object> manualTriggerFirmwareVerify(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId){
        deviceApplicationFormService.manualTriggerFirmwareVerify(applicationFormId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/manual-trigger-sync")
    @Log("手动触发同步")
    @ApiOperation("手动触发同步")
    @PreAuthorize("@el.check('deviceApplicationForm:manual')")
    public ResponseEntity<Object> manualTriggerSync(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId){
        deviceApplicationFormService.manualTriggerSync(applicationFormId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/manual-complete-process")
    @Log("手动完成流程")
    @ApiOperation("手动完成流程")
    @PreAuthorize("@el.check('deviceApplicationForm:manual')")
    public ResponseEntity<Object> manualCompleteProcess(
            @ApiParam(value = "申请单ID") @RequestParam Integer applicationFormId){
        deviceApplicationFormService.manualCompleteProcess(applicationFormId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}