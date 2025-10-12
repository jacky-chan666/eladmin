/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.repository;

import com.tplink.smb.common.data.management.system.domain.S3Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface S3StorageRepository
    extends JpaRepository<S3Storage, Long>, JpaSpecificationExecutor<S3Storage> {

  /**
   * 根据ID查询文件路径
   *
   * @param id 文件ID
   * @return 文件路径
   */
  @Query(value = "SELECT file_path FROM s3_storage WHERE id = ?1", nativeQuery = true)
  String selectFilePathById(Long id);
}
