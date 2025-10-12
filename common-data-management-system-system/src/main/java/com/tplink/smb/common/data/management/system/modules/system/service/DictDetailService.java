/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service;

import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDetailDto;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDetailQueryCriteria;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import com.tplink.smb.common.data.management.system.modules.system.domain.DictDetail;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface DictDetailService {

  /**
   * 创建
   *
   * @param resources /
   */
  void create(DictDetail resources);

  /**
   * 编辑
   *
   * @param resources /
   */
  void update(DictDetail resources);

  /**
   * 删除
   *
   * @param id /
   */
  void delete(Long id);

  /**
   * 分页查询
   *
   * @param criteria 条件
   * @param pageable 分页参数
   * @return /
   */
  PageResult<DictDetailDto> queryAll(DictDetailQueryCriteria criteria, Pageable pageable);

  /**
   * 根据字典名称获取字典详情
   *
   * @param name 字典名称
   * @return /
   */
  List<DictDetailDto> getDictByName(String name);
}
