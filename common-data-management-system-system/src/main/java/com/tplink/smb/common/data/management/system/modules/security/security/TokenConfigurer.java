/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.security.security;

import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.modules.security.config.SecurityProperties;
import com.tplink.smb.common.data.management.system.modules.security.service.OnlineUserService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RequiredArgsConstructor
public class TokenConfigurer
    extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final TokenProvider tokenProvider;
  private final SecurityProperties properties;
  private final OnlineUserService onlineUserService;

  @Override
  public void configure(HttpSecurity http) {
    TokenFilter customFilter = new TokenFilter(tokenProvider, properties, onlineUserService);
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
