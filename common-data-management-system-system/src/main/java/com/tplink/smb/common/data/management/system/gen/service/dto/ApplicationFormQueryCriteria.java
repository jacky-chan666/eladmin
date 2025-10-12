/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.dto;

import com.tplink.smb.common.data.management.system.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class ApplicationFormQueryCriteria {

  /** 模糊 */
  @Query(type = Query.Type.INNER_LIKE)
  @ApiModelProperty(value = "申请单UUID")
  private String uuid;

  /** 模糊 */
  @Query(type = Query.Type.INNER_LIKE)
  @ApiModelProperty(value = "申请人姓名")
  private String applicantUserName;

  /** 模糊 */
  @Query(type = Query.Type.INNER_LIKE)
  @ApiModelProperty(value = "申请单类型：新增，修改，上线，下线")
  private Integer applicationType;

  /** 模糊 */
  @Query(type = Query.Type.INNER_LIKE)
  @ApiModelProperty(value = "申请单数据类型：omada，vigi，adblocking")
  private Integer applicationDataType;

  /** 模糊 */
  @Query(type = Query.Type.INNER_LIKE)
  @ApiModelProperty(value = "申请单标题")
  private String applicationTitle;

  /** 准确 */
  @Query(type = Query.Type.EQUAL)
  @ApiModelProperty(value = "审核人姓名")
  private String approverUserName; // 当前待审批人姓名
}
