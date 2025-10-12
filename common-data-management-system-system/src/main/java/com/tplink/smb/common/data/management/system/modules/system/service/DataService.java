/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service;

import com.tplink.smb.common.data.management.system.modules.system.service.dto.UserDto;

import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface DataService {

  /**
   * 获取数据权限
   *
   * @param user /
   * @return /
   */
  List<Long> getDeptIds(UserDto user);
}
