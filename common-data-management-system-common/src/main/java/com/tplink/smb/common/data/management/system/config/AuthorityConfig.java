/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.config;

import com.tplink.smb.common.data.management.system.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Service(value = "el")
public class AuthorityConfig {

  /**
   * 判断接口是否有权限
   *
   * @param permissions 权限
   * @return /
   */
  public Boolean check(String... permissions) {
    // 获取当前用户的所有权限
    List<String> elPermissions =
        SecurityUtils.getCurrentUser().getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    // 判断当前用户的所有权限是否包含接口上定义的权限
    return elPermissions.contains("admin")
        || Arrays.stream(permissions).anyMatch(elPermissions::contains);
  }
}
