/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Entity
@Data
@Table(name = "application_form")
public class ApplicationForm implements Serializable {

  // 状态常量定义
  public static final Integer STATUS_DRAFT = -1; // 草稿
  public static final Integer STATUS_SUBMITTED = 0; // 已提交（新增状态）
  public static final Integer STATUS_PENDING = 1; // 待审批（原0改为1）
  public static final Integer STATUS_APPROVED = 2; // 审批通过（原1改为2）
  public static final Integer STATUS_COMPLETED = 3;
  public static final Integer STATUS_FIRMWARE_VERIFY = 4; // 固件校验中（原2改为3）
  public static final Integer STATUS_FIRMWARE_FAILED = 5; // 固件校验失败（原3改为4）
  public static final Integer STATUS_GRAY_SYNCING = 6; // 同步中（原4改为5）
  public static final Integer STATUS_GRAY_SYNC_FAILED = 7; // 同步失败（原5改为6）
  public static final Integer STATUS_SYNCING = 8; // 同步中（原4改为5）
  public static final Integer STATUS_SYNC_FAILED =
      9; // 同步失败（原5改为6）    public static final Integer STATUS_COMPLETED = 7;     // 已完成（原6改为7）
  public static final Integer STATUS_REJECTED = 10; // 已驳回（原7改为8）
  public static final Integer STATUS_AUTO_PROCESSING = 11; // 自动处理中（原8改为9）
  public static final Integer STATUS_AUTO_FAILED = 12; // 自动处理失败（原9改为10）
  public static final Integer STATUS_MANUAL_TRIGGERED = 13; // 手动触发（原10改为11）
  public static final Integer STATUS_WITHDRAWN = 14; // 已撤回（新增状态）

  // 申请单类型常量
  public static final int APPLICATION_TYPE_ADD = 1;
  public static final int APPLICATION_TYPE_EDIT = 2;
  public static final int APPLICATION_TYPE_ONLINE = 3;
  public static final int APPLICATION_TYPE_OFFLINE = 4;

  // 申请单数据类型常量
  public static final int APPLICATION_DATATYPE_DEVICEINFO = 1;
  public static final int APPLICATION_DATATYPE_GATEWAYINFO = 2;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "`id`")
  @ApiModelProperty(value = "id")
  private Integer id;

  @Column(name = "`uuid`", unique = true, nullable = false)
  @ApiModelProperty(value = "申请单UUID")
  private String uuid;

  @Column(name = "`applicant_username`", nullable = false)
  @NotBlank
  @ApiModelProperty(value = "申请人用户名")
  private String applicantUserName;

  @Column(name = "`department`")
  @ApiModelProperty(value = "所属部门")
  private String department;

  @Column(name = "`application_data_id`", unique = true)
  @ApiModelProperty(value = "申请数据id")
  private Integer applicationDataId;

  @Column(name = "`application_type`", nullable = false)
  @NotNull
  @ApiModelProperty(value = "申请单类型：新增，修改，上线，下线")
  private Integer applicationType;

  // 修改 ApplicationForm.java 中的 applicationDataType 字段注释
  @Column(name = "`application_data_type`", nullable = false)
  @NotNull
  @ApiModelProperty(value = "申请单数据类型：1-deviceInfo，2-gatewayInfo")
  private Integer applicationDataType;

  @Column(name = "`application_title`", nullable = false)
  @NotBlank
  @ApiModelProperty(value = "申请单标题")
  private String applicationTitle;

  @Column(name = "`application_reason`")
  @ApiModelProperty(value = "申请理由")
  private String applicationReason;

  @Column(name = "`status`", nullable = false)
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

  @Column(name = "`data_details`")
  @ApiModelProperty(value = "设备信息详情（JSON格式存储）")
  private String dataDetails;

  @Column(name = "`created_at`")
  @ApiModelProperty(value = "createdAt")
  private Timestamp createdAt;

  @Column(name = "`updated_at`")
  @ApiModelProperty(value = "updatedAt")
  private Timestamp updatedAt;

  public void copy(ApplicationForm source) {
    BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
  }
}
