/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system;

import com.tplink.smb.common.data.management.system.annotation.rest.AnonymousGetMapping;
import com.tplink.smb.common.data.management.system.utils.SpringBeanHolder;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Slf4j
@EnableAsync
@RestController
@Api(hidden = true)
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AppRun {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(AppRun.class);
    // 监控应用的PID，启动时可指定PID路径：--spring.pid.file=/home/eladmin/app.pid
    // 或者在 application.yml 添加文件路径，方便 kill，kill `cat /home/eladmin/app.pid`
    springApplication.addListeners(new ApplicationPidFileWriter());
    ConfigurableApplicationContext context = springApplication.run(args);
    String port = context.getEnvironment().getProperty("server.port");
    log.info("---------------------------------------------");
    log.info("Local: http://localhost:{}", port);
    log.info("Swagger: http://localhost:{}/doc.html", port);
    log.info("---------------------------------------------");
  }

  @Bean
  public SpringBeanHolder springContextHolder() {
    return new SpringBeanHolder();
  }

  /**
   * 访问首页提示
   *
   * @return /
   */
  @AnonymousGetMapping("/")
  public String index() {
    return "Backend service started successfully";
  }
}
