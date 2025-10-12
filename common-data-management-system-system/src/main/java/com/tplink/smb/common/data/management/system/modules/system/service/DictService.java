/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service;

import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDto;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictQueryCriteria;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import com.tplink.smb.common.data.management.system.modules.system.domain.Dict;
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
public interface DictService {

  /**
   * 分页查询
   *
   * @param criteria 条件
   * @param pageable 分页参数
   * @return /
   */
  PageResult<DictDto> queryAll(DictQueryCriteria criteria, Pageable pageable);

  /**
   * 查询全部数据
   *
   * @param dict /
   * @return /
   */
  List<DictDto> queryAll(DictQueryCriteria dict);

  /**
   * 创建
   *
   * @param resources /
   * @return /
   */
  void create(Dict resources);

  /**
   * 编辑
   *
   * @param resources /
   */
  void update(Dict resources);

  /**
   * 删除
   *
   * @param ids /
   */
  void delete(Set<Long> ids);

  /**
   * 导出数据
   *
   * @param queryAll 待导出的数据
   * @param response /
   * @throws IOException /
   */
  void download(List<DictDto> queryAll, HttpServletResponse response) throws IOException;
}
