/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class ApplicationFormDto implements Serializable {

  @ApiModelProperty(value = "id")
  private Integer id;

  @ApiModelProperty(value = "申请单UUID")
  private String uuid;

  @ApiModelProperty(value = "申请人姓名")
  private String applicantUserName;

  @ApiModelProperty(value = "所属部门")
  private String department;

  @ApiModelProperty(value = "申请数据id")
  private Integer applicationDataId;

  @ApiModelProperty(value = "申请单类型：新增，修改，上线，下线")
  private Integer applicationType;

  // 修改 ApplicationFormDto.java 中的 applicationDataType 字段注释
  @ApiModelProperty(value = "申请单数据类型：1-deviceInfo，2-gatewayInfo")
  private Integer applicationDataType;

  @ApiModelProperty(value = "申请单标题")
  private String applicationTitle;

  @ApiModelProperty(value = "申请理由")
  private String applicationReason;

  @ApiModelProperty(value = "申请状态")
  private Integer status;

  @ApiModelProperty(value = "测试接口人")
  private String testContact;

  @ApiModelProperty(value = "测试组长")
  private String testLeader;

  @ApiModelProperty(value = "研发接口人")
  private String devContact;

  @ApiModelProperty(value = "研发组长")
  private String devLeader;

  @ApiModelProperty(value = "测试接口人审批状态")
  private Integer testContactApproval;

  @ApiModelProperty(value = "测试组长审批状态")
  private Integer testLeaderApproval;

  @ApiModelProperty(value = "研发接口人审批状态")
  private Integer devContactApproval;

  @ApiModelProperty(value = "研发组长审批状态")
  private Integer devLeaderApproval;

  @ApiModelProperty(value = "测试接口人审批意见")
  private String testContactComment;

  @ApiModelProperty(value = "测试组长审批意见")
  private String testLeaderComment;

  @ApiModelProperty(value = "研发接口人审批意见")
  private String devContactComment;

  @ApiModelProperty(value = "研发组长意见审批")
  private String devLeaderComment;

  @ApiModelProperty(value = "当前审核人列表（JSON格式存储）")
  private String currentApprovers;

  @ApiModelProperty(value = "审核历史表（JSON格式存储，记录每次提交的审批人，审批状态和审批意见）")
  private String approvalHistory;

  // 在 ApplicationFormDto.java 中添加以下字段
  @ApiModelProperty(value = "设备信息详情（JSON格式存储）")
  private String dataDetails;

  @ApiModelProperty(value = "createdAt")
  private Timestamp createdAt;

  @ApiModelProperty(value = "updatedAt")
  private Timestamp updatedAt;
}
