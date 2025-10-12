/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service.impl;

import com.tplink.smb.common.data.management.system.domain.GenConfig;
import com.tplink.smb.common.data.management.system.repository.GenConfigRepository;
import com.tplink.smb.common.data.management.system.service.GenConfigService;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "all"})
public class GenConfigServiceImpl implements GenConfigService {

  private final GenConfigRepository genConfigRepository;

  @Override
  public GenConfig find(String tableName) {
    GenConfig genConfig = genConfigRepository.findByTableName(tableName);
    if (genConfig == null) {
      return new GenConfig(tableName);
    }
    return genConfig;
  }

  @Override
  public GenConfig update(String tableName, GenConfig genConfig) {
    String separator = File.separator;
    String[] paths;
    String symbol = "\\";
    if (symbol.equals(separator)) {
      paths = genConfig.getPath().split("\\\\");
    } else {
      paths = genConfig.getPath().split(File.separator);
    }
    StringBuilder api = new StringBuilder();
    for (String path : paths) {
      api.append(path);
      api.append(separator);
      if ("src".equals(path)) {
        api.append("api");
        break;
      }
    }
    genConfig.setApiPath(api.toString());
    return genConfigRepository.save(genConfig);
  }
}
