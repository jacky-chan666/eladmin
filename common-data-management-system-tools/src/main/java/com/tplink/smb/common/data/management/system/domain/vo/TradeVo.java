/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class TradeVo {

  @NotBlank
  @ApiModelProperty(value = "商品描述")
  private String body;

  @NotBlank
  @ApiModelProperty(value = "商品名称")
  private String subject;

  @ApiModelProperty(value = "商户订单号", hidden = true)
  private String outTradeNo;

  @ApiModelProperty(value = "第三方订单号", hidden = true)
  private String tradeNo;

  @NotBlank
  @ApiModelProperty(value = "价格")
  private String totalAmount;

  @ApiModelProperty(value = "订单状态,已支付，未支付，作废", hidden = true)
  private String state;

  @ApiModelProperty(value = "创建时间", hidden = true)
  private Timestamp createTime;

  @ApiModelProperty(value = "作废时间", hidden = true)
  private Date cancelTime;
}
