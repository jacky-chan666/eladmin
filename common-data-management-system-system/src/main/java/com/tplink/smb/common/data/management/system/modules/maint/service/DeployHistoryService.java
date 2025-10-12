/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service;

import com.tplink.smb.common.data.management.system.modules.maint.domain.DeployHistory;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.DeployHistoryDto;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.DeployHistoryQueryCriteria;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface DeployHistoryService {

  /**
   * 分页查询
   *
   * @param criteria 条件
   * @param pageable 分页参数
   * @return /
   */
  PageResult<DeployHistoryDto> queryAll(DeployHistoryQueryCriteria criteria, Pageable pageable);

  /**
   * 查询全部
   *
   * @param criteria 条件
   * @return /
   */
  List<DeployHistoryDto> queryAll(DeployHistoryQueryCriteria criteria);

  /**
   * 根据ID查询
   *
   * @param id /
   * @return /
   */
  DeployHistoryDto findById(String id);

  /**
   * 创建
   *
   * @param resources /
   */
  void create(DeployHistory resources);

  /**
   * 删除
   *
   * @param ids /
   */
  void delete(Set<String> ids);

  /**
   * 导出数据
   *
   * @param queryAll /
   * @param response /
   * @throws IOException /
   */
  void download(List<DeployHistoryDto> queryAll, HttpServletResponse response) throws IOException;
}
