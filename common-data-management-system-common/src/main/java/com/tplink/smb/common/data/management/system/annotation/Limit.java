/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.annotation;

import com.tplink.smb.common.data.management.system.aspect.LimitType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

  // 资源名称，用于描述接口功能
  String name() default "";

  // 资源 key
  String key() default "";

  // key prefix
  String prefix() default "";

  // 时间的，单位秒
  int period();

  // 限制访问次数
  int count();

  // 限制类型
  LimitType limitType() default LimitType.CUSTOMER;
}
