/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PendingApprovalDto {

  @ApiModelProperty(value = "申请单信息")
  private ApplicationFormDto applicationForm;

  @ApiModelProperty(value = "审批轮次")
  private Integer round;

  @ApiModelProperty(value = "步骤顺序")
  private Integer stepOrder;
}
