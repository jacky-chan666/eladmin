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
package me.zhengjie.gen.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Chen Jiayuan
* @date 2025-09-18
**/
@Entity
@Data
@Table(name="device_application_form")
public class DeviceApplicationForm implements Serializable {

    // 状态常量定义
    public static final Integer STATUS_DRAFT = -1;        // 草稿
    public static final Integer STATUS_PENDING = 0;       // 待审批
    public static final Integer STATUS_APPROVED = 1;      // 审批通过
    public static final Integer STATUS_FIRMWARE_VERIFY = 2; // 固件校验中
    public static final Integer STATUS_FIRMWARE_FAILED = 3; // 固件校验失败
    public static final Integer STATUS_SYNCING = 4;       // 同步中
    public static final Integer STATUS_SYNC_FAILED = 5;   // 同步失败
    public static final Integer STATUS_COMPLETED = 6;     // 已完成
    public static final Integer STATUS_REJECTED = 7;      // 已驳回
    public static final Integer STATUS_AUTO_PROCESSING = 8; // 自动处理中
    public static final Integer STATUS_AUTO_FAILED = 9;   // 自动处理失败
    public static final Integer STATUS_MANUAL_TRIGGERED = 10; // 手动触发

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`applicant_id`",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "申请单UUID")
    private String applicantId;

    @Column(name = "`applicant_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @Column(name = "`department`")
    @ApiModelProperty(value = "所属部门")
    private String department;

    @Column(name = "`application_date`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "申请日期")
    private Timestamp applicationDate;

    @Column(name = "`application_data_id`",unique = true)
    @ApiModelProperty(value = "申请数据id")
    private Integer applicationDataId;

    @Column(name = "`application_type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "申请单类型：新增，修改，上线，下线")
    private Integer applicationType;

    @Column(name = "`application_data_type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "申请单数据类型：omada，vigi，adblocking")
    private Integer applicationDataType;

    @Column(name = "`application_title`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "申请单标题")
    private String applicationTitle;

    @Column(name = "`application_reason`")
    @ApiModelProperty(value = "申请理由")
    private String applicationReason;

    @Column(name = "`status`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "申请状态")
    private Integer status;

    @Column(name = "`test_contact`")
    @ApiModelProperty(value = "测试接口人")
    private String testContact;

    @Column(name = "`test_leader`")
    @ApiModelProperty(value = "测试组长")
    private String testLeader;

    @Column(name = "`dev_contact`")
    @ApiModelProperty(value = "研发接口人")
    private String devContact;

    @Column(name = "`dev_leader`")
    @ApiModelProperty(value = "研发组长")
    private String devLeader;

    @Column(name = "`test_contact_approval`")
    @ApiModelProperty(value = "测试接口人审批状态")
    private Integer testContactApproval;

    @Column(name = "`test_leader_approval`")
    @ApiModelProperty(value = "测试组长审批状态")
    private Integer testLeaderApproval;

    @Column(name = "`dev_contact_approval`")
    @ApiModelProperty(value = "研发接口人审批状态")
    private Integer devContactApproval;

    @Column(name = "`dev_leader_approval`")
    @ApiModelProperty(value = "研发组长审批状态")
    private Integer devLeaderApproval;

    @Column(name = "`test_contact_comment`")
    @ApiModelProperty(value = "测试接口人审批意见")
    private String testContactComment;

    @Column(name = "`test_leader_comment`")
    @ApiModelProperty(value = "测试组长审批意见")
    private String testLeaderComment;

    @Column(name = "`dev_contact_comment`")
    @ApiModelProperty(value = "研发接口人审批意见")
    private String devContactComment;

    @Column(name = "`dev_leader_comment`")
    @ApiModelProperty(value = "研发组长意见审批")
    private String devLeaderComment;

    @Column(name = "`current_approvers`")
    @ApiModelProperty(value = "当前审核人列表（JSON格式存储）")
    private String currentApprovers;

    @Column(name = "`approval_history`")
    @ApiModelProperty(value = "审核历史表（JSON格式存储，记录每次提交的审批人，审批状态和审批意见）")
    private String approvalHistory;

    @Column(name = "`device_info_details`")
    @ApiModelProperty(value = "设备信息详情（JSON格式存储）")
    private String deviceInfoDetails;

    @Column(name = "`created_at`")
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "`updated_at`")
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;

    public void copy(DeviceApplicationForm source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
