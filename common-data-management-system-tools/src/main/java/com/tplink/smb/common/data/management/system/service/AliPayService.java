/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service;

import com.tplink.smb.common.data.management.system.domain.vo.TradeVo;
import com.tplink.smb.common.data.management.system.domain.AlipayConfig;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface AliPayService {

  /**
   * 查询配置
   *
   * @return AlipayConfig
   */
  AlipayConfig find();

  /**
   * 更新配置
   *
   * @param alipayConfig 支付宝配置
   * @return AlipayConfig
   */
  AlipayConfig config(AlipayConfig alipayConfig);

  /**
   * 处理来自PC的交易请求
   *
   * @param alipay 支付宝配置
   * @param trade 交易详情
   * @return String
   * @throws Exception 异常
   */
  String toPayAsPc(AlipayConfig alipay, TradeVo trade) throws Exception;

  /**
   * 处理来自手机网页的交易请求
   *
   * @param alipay 支付宝配置
   * @param trade 交易详情
   * @return String
   * @throws Exception 异常
   */
  String toPayAsWeb(AlipayConfig alipay, TradeVo trade) throws Exception;
}
