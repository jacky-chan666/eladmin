/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.tplink.smb.common.data.management.system.annotation.Limit;
import com.tplink.smb.common.data.management.system.annotation.rest.AnonymousGetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequestMapping("/api/limit")
@Api(tags = "系统：限流测试管理")
public class LimitController {

  private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

  /** 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis的键名为 limit_test， */
  @AnonymousGetMapping
  @ApiOperation("测试")
  @Limit(key = "test", period = 60, count = 10, name = "testLimit", prefix = "limit")
  public int testLimit() {
    return ATOMIC_INTEGER.incrementAndGet();
  }
}
