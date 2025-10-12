/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service.impl;

import com.tplink.smb.common.data.management.system.modules.maint.repository.AppRepository;
import com.tplink.smb.common.data.management.system.utils.*;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.exception.BadRequestException;
import com.tplink.smb.common.data.management.system.modules.maint.domain.App;
import com.tplink.smb.common.data.management.system.modules.maint.service.AppService;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.AppDto;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.AppQueryCriteria;
import com.tplink.smb.common.data.management.system.modules.maint.service.mapstruct.AppMapper;
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
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

  private final AppRepository appRepository;
  private final AppMapper appMapper;

  @Override
  public PageResult<AppDto> queryAll(AppQueryCriteria criteria, Pageable pageable) {
    Page<App> page =
        appRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return PageUtil.toPage(page.map(appMapper::toDto));
  }

  @Override
  public List<AppDto> queryAll(AppQueryCriteria criteria) {
    return appMapper.toDto(
        appRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
  }

  @Override
  public AppDto findById(Long id) {
    App app = appRepository.findById(id).orElseGet(App::new);
    ValidationUtil.isNull(app.getId(), "App", "id", id);
    return appMapper.toDto(app);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void create(App resources) {
    // 验证应用名称是否存在恶意攻击payload，https://github.com/elunez/eladmin/issues/873
    String appName = resources.getName();
    if (appName.contains(";") || appName.contains("|") || appName.contains("&")) {
      throw new IllegalArgumentException("非法的应用名称，请勿包含[; | &]等特殊字符");
    }
    verification(resources);
    appRepository.save(resources);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(App resources) {
    // 验证应用名称是否存在恶意攻击payload，https://github.com/elunez/eladmin/issues/873
    String appName = resources.getName();
    if (appName.contains(";") || appName.contains("|") || appName.contains("&")) {
      throw new IllegalArgumentException("非法的应用名称，请勿包含[; | &]等特殊字符");
    }
    verification(resources);
    App app = appRepository.findById(resources.getId()).orElseGet(App::new);
    ValidationUtil.isNull(app.getId(), "App", "id", resources.getId());
    app.copy(resources);
    appRepository.save(app);
  }

  private void verification(App resources) {
    String opt = "/opt";
    String home = "/home";
    if (!(resources.getUploadPath().startsWith(opt)
        || resources.getUploadPath().startsWith(home))) {
      throw new BadRequestException("文件只能上传在opt目录或者home目录 ");
    }
    if (!(resources.getDeployPath().startsWith(opt)
        || resources.getDeployPath().startsWith(home))) {
      throw new BadRequestException("文件只能部署在opt目录或者home目录 ");
    }
    if (!(resources.getBackupPath().startsWith(opt)
        || resources.getBackupPath().startsWith(home))) {
      throw new BadRequestException("文件只能备份在opt目录或者home目录 ");
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Set<Long> ids) {
    for (Long id : ids) {
      appRepository.deleteById(id);
    }
  }

  @Override
  public void download(List<AppDto> queryAll, HttpServletResponse response) throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (AppDto appDto : queryAll) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("应用名称", appDto.getName());
      map.put("端口", appDto.getPort());
      map.put("上传目录", appDto.getUploadPath());
      map.put("部署目录", appDto.getDeployPath());
      map.put("备份目录", appDto.getBackupPath());
      map.put("启动脚本", appDto.getStartScript());
      map.put("部署脚本", appDto.getDeployScript());
      map.put("创建日期", appDto.getCreateTime());
      list.add(map);
    }
    FileUtil.downloadExcel(list, response);
  }
}
