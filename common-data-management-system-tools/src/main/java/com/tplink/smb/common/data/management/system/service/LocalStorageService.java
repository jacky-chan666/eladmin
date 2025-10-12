/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service;

import com.tplink.smb.common.data.management.system.service.dto.LocalStorageDto;
import com.tplink.smb.common.data.management.system.service.dto.LocalStorageQueryCriteria;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import com.tplink.smb.common.data.management.system.domain.LocalStorage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface LocalStorageService {

  /**
   * 分页查询
   *
   * @param criteria 条件
   * @param pageable 分页参数
   * @return /
   */
  PageResult<LocalStorageDto> queryAll(LocalStorageQueryCriteria criteria, Pageable pageable);

  /**
   * 查询全部数据
   *
   * @param criteria 条件
   * @return /
   */
  List<LocalStorageDto> queryAll(LocalStorageQueryCriteria criteria);

  /**
   * 根据ID查询
   *
   * @param id /
   * @return /
   */
  LocalStorageDto findById(Long id);

  /**
   * 上传
   *
   * @param name 文件名称
   * @param file 文件
   * @return /
   */
  LocalStorage create(String name, MultipartFile file);

  /**
   * 编辑
   *
   * @param resources 文件信息
   */
  void update(LocalStorage resources);

  /**
   * 多选删除
   *
   * @param ids /
   */
  void deleteAll(Long[] ids);

  /**
   * 导出数据
   *
   * @param localStorageDtos 待导出的数据
   * @param response /
   * @throws IOException /
   */
  void download(List<LocalStorageDto> localStorageDtos, HttpServletResponse response)
      throws IOException;
}
