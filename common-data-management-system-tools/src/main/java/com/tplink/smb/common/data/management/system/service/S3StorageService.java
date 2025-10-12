/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service;

import com.tplink.smb.common.data.management.system.service.dto.S3StorageQueryCriteria;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import com.tplink.smb.common.data.management.system.domain.S3Storage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface S3StorageService {

  /**
   * 查询数据分页
   *
   * @param criteria 条件
   * @param pageable 分页参数
   * @return PageResult
   */
  PageResult<S3Storage> queryAll(S3StorageQueryCriteria criteria, Pageable pageable);

  /**
   * 查询所有数据不分页
   *
   * @param criteria 条件参数
   * @return List<S3StorageDto>
   */
  List<S3Storage> queryAll(S3StorageQueryCriteria criteria);

  /**
   * 多选删除
   *
   * @param ids /
   */
  void deleteAll(List<Long> ids);

  /**
   * 导出数据
   *
   * @param all 待导出的数据
   * @param response /
   * @throws IOException /
   */
  void download(List<S3Storage> all, HttpServletResponse response) throws IOException;

  /**
   * 私有化下载，仅供参考，还有许多方式
   *
   * @param id 文件ID
   */
  Map<String, String> privateDownload(Long id);

  /**
   * 上传文件
   *
   * @param file 上传的文件
   * @return S3Storage 对象，包含文件存储信息
   */
  S3Storage upload(MultipartFile file);

  /**
   * 根据ID获取文件信息
   *
   * @param id 文件ID
   * @return S3Storage 对象，包含文件存储信息
   */
  S3Storage getById(Long id);

  String generatePresignedUrl(String filePath, long expireInSeconds);
}
