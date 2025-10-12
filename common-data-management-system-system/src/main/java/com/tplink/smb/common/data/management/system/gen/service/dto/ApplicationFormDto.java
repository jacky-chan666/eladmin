/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

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

  @ApiModelProperty(value = "申请单数据类型：1-deviceInfo，2-gatewayInfo")
  private Integer applicationDataType;

  @ApiModelProperty(value = "申请单标题")
  private String applicationTitle;

  @ApiModelProperty(value = "申请理由")
  private String applicationReason;

  @ApiModelProperty(value = "审批轮次")
  private Integer round;

  @ApiModelProperty(value = "申请状态")
  private Integer status;

  // 删除原有的审批人字段，新增6个审批人字段
  @ApiModelProperty(value = "审批人1")
  private String approver1;

  @ApiModelProperty(value = "审批人1层级")
  private Integer approver1Level;

  @ApiModelProperty(value = "审批人2")
  private String approver2;

  @ApiModelProperty(value = "审批人2层级")
  private Integer approver2Level;

  @ApiModelProperty(value = "审批人3")
  private String approver3;

  @ApiModelProperty(value = "审批人3层级")
  private Integer approver3Level;

  @ApiModelProperty(value = "审批人4")
  private String approver4;

  @ApiModelProperty(value = "审批人4层级")
  private Integer approver4Level;

  @ApiModelProperty(value = "审批人5")
  private String approver5;

  @ApiModelProperty(value = "审批人5层级")
  private Integer approver5Level;

  @ApiModelProperty(value = "审批人6")
  private String approver6;

  @ApiModelProperty(value = "审批人6层级")
  private Integer approver6Level;

  // 为每个审批人添加状态和意见字段
  @ApiModelProperty(value = "审批人1状态")
  private Integer approver1Status;

  @ApiModelProperty(value = "审批人1意见")
  private String approver1Comment;

  @ApiModelProperty(value = "审批人2状态")
  private Integer approver2Status;

  @ApiModelProperty(value = "审批人2意见")
  private String approver2Comment;

  @ApiModelProperty(value = "审批人3状态")
  private Integer approver3Status;

  @ApiModelProperty(value = "审批人3意见")
  private String approver3Comment;

  @ApiModelProperty(value = "审批人4状态")
  private Integer approver4Status;

  @ApiModelProperty(value = "审批人4意见")
  private String approver4Comment;

  @ApiModelProperty(value = "审批人5状态")
  private Integer approver5Status;

  @ApiModelProperty(value = "审批人5意见")
  private String approver5Comment;

  @ApiModelProperty(value = "审批人6状态")
  private Integer approver6Status;

  @ApiModelProperty(value = "审批人6意见")
  private String approver6Comment;

  // DTO中保留这两个字段用于前端展示，但不在实体中存储
  @ApiModelProperty(value = "当前审核人列表（JSON格式存储）")
  private String currentApprovers;

  @ApiModelProperty(value = "审批记录（JSON格式存储）")
  private String approvalRecords;

  // 在 ApplicationFormDto.java 中添加以下字段
  @ApiModelProperty(value = "设备信息详情（JSON格式存储）")
  private String dataDetails;

  @ApiModelProperty(value = "createdAt")
  private Timestamp createdAt;

  @ApiModelProperty(value = "updatedAt")
  private Timestamp updatedAt;
}
