/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Slf4j
@Service
public class TestTask {

  public void run() {
    log.info("run 执行成功");
  }

  public void run1(String str) {
    log.info("run1 执行成功，参数为： {}", str);
  }

  public void run2() {
    log.info("run2 执行成功");
  }
}
