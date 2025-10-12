/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.quartz.config;

import com.tplink.smb.common.data.management.system.modules.quartz.domain.QuartzJob;
import com.tplink.smb.common.data.management.system.modules.quartz.repository.QuartzJobRepository;
import com.tplink.smb.common.data.management.system.modules.quartz.utils.QuartzManage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {
  private static final Logger log = LoggerFactory.getLogger(JobRunner.class);
  private final QuartzJobRepository quartzJobRepository;
  private final QuartzManage quartzManage;

  /**
   * 项目启动时重新激活启用的定时任务
   *
   * @param applicationArguments /
   */
  @Override
  public void run(ApplicationArguments applicationArguments) {
    List<QuartzJob> quartzJobs = quartzJobRepository.findByIsPauseIsFalse();
    quartzJobs.forEach(quartzManage::addJob);
    log.info("Timing task injection complete");
  }
}
