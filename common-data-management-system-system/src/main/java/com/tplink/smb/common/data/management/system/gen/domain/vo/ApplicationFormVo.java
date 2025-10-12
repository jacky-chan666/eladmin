/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description of this file
 *
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/19
 */
@Data
public class ApplicationFormVo {

  @ApiModelProperty("申请单ID,主键ID")
  private Integer id;

  @ApiModelProperty("申请单编号（系统生成，可选）")
  private String uuid;

  @ApiModelProperty("申请单标题")
  private String applicationTitle;

  @ApiModelProperty(value = "申请理由")
  private String applicationReason;

  @ApiModelProperty(value = "申请单类型：新增，修改，上线，下线")
  private Integer applicationType;

  @ApiModelProperty(value = "申请单数据类型：1-deviceInfo，2-gatewayInfo")
  private Integer applicationDataType;

  @ApiModelProperty(value = "设备信息数据的id")
  private Integer applicationDataId;

  // 删除原有的审批人字段，新增6个审批人字段
  @ApiModelProperty("审批人1")
  private String approver1;

  @ApiModelProperty("审批人1层级")
  private Integer approver1Level;

  @ApiModelProperty("审批人2")
  private String approver2;

  @ApiModelProperty("审批人2层级")
  private Integer approver2Level;

  @ApiModelProperty("审批人3")
  private String approver3;

  @ApiModelProperty("审批人3层级")
  private Integer approver3Level;

  @ApiModelProperty("审批人4")
  private String approver4;

  @ApiModelProperty("审批人4层级")
  private Integer approver4Level;

  @ApiModelProperty("审批人5")
  private String approver5;

  @ApiModelProperty("审批人5层级")
  private Integer approver5Level;

  @ApiModelProperty("审批人6")
  private String approver6;

  @ApiModelProperty("审批人6层级")
  private Integer approver6Level;

  @ApiModelProperty("申请人用户名（当前登录用户）")
  private String applicantUserName;

  @ApiModelProperty("数据详细信息（JSON格式字符串）")
  private String dataDetails;
}
