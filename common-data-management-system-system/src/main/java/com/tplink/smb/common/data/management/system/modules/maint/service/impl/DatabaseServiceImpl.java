/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service.impl;

import cn.hutool.core.util.IdUtil;
import com.tplink.smb.common.data.management.system.modules.maint.repository.DatabaseRepository;
import com.tplink.smb.common.data.management.system.modules.maint.service.mapstruct.DatabaseMapper;
import com.tplink.smb.common.data.management.system.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tplink.smb.common.data.management.system.modules.maint.domain.Database;
import com.tplink.smb.common.data.management.system.modules.maint.service.DatabaseService;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.DatabaseDto;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.DatabaseQueryCriteria;
import com.tplink.smb.common.data.management.system.modules.maint.util.SqlUtils;
import com.tplink.smb.common.data.management.system.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl implements DatabaseService {

  private final DatabaseRepository databaseRepository;
  private final DatabaseMapper databaseMapper;

  @Override
  public PageResult<DatabaseDto> queryAll(DatabaseQueryCriteria criteria, Pageable pageable) {
    Page<Database> page =
        databaseRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return PageUtil.toPage(page.map(databaseMapper::toDto));
  }

  @Override
  public List<DatabaseDto> queryAll(DatabaseQueryCriteria criteria) {
    return databaseMapper.toDto(
        databaseRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
  }

  @Override
  public DatabaseDto findById(String id) {
    Database database = databaseRepository.findById(id).orElseGet(Database::new);
    ValidationUtil.isNull(database.getId(), "Database", "id", id);
    return databaseMapper.toDto(database);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void create(Database resources) {
    resources.setId(IdUtil.simpleUUID());
    databaseRepository.save(resources);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(Database resources) {
    Database database = databaseRepository.findById(resources.getId()).orElseGet(Database::new);
    ValidationUtil.isNull(database.getId(), "Database", "id", resources.getId());
    database.copy(resources);
    databaseRepository.save(database);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Set<String> ids) {
    for (String id : ids) {
      databaseRepository.deleteById(id);
    }
  }

  @Override
  public boolean testConnection(Database resources) {
    try {
      return SqlUtils.testConnection(
          resources.getJdbcUrl(), resources.getUserName(), resources.getPwd());
    } catch (Exception e) {
      log.error(e.getMessage());
      return false;
    }
  }

  @Override
  public void download(List<DatabaseDto> queryAll, HttpServletResponse response)
      throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (DatabaseDto databaseDto : queryAll) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("数据库名称", databaseDto.getName());
      map.put("数据库连接地址", databaseDto.getJdbcUrl());
      map.put("用户名", databaseDto.getUserName());
      map.put("创建日期", databaseDto.getCreateTime());
      list.add(map);
    }
    FileUtil.downloadExcel(list, response);
  }
}
