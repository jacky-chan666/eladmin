/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;

@Data
public class ApprovalRecordDto implements Serializable {

  @ApiModelProperty(value = "ID")
  private Integer id;

  @ApiModelProperty(value = "申请单ID")
  private Integer applicationFormId;

  @ApiModelProperty(value = "审批轮次")
  private Integer round;

  @ApiModelProperty(value = "步骤顺序")
  private Integer stepOrder;

  @ApiModelProperty(value = "审批人角色")
  private String approverRole;

  @ApiModelProperty(value = "审批人姓名")
  private String approverUserName;

  @ApiModelProperty(value = "审批状态：0-待审批，1-通过，2-驳回")
  private Integer approvalStatus;

  @ApiModelProperty(value = "审批意见")
  private String comment;

  @ApiModelProperty(value = "审批时间")
  private Timestamp approvedAt;

  @ApiModelProperty(value = "审批状态文本")
  private String approvalStatusText;
}
