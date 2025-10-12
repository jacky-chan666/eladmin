/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.config.webConfig;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Configuration(proxyBeanMethods = false)
public class QueryCustomizer implements TomcatConnectorCustomizer {
  @Override
  public void customize(Connector connector) {
    connector.setProperty("relaxedQueryChars", "[]{}");
  }
}
