/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.tplink.smb.common.data.management.system.modules.system.service.mapstruct.DictDetailMapper;
import com.tplink.smb.common.data.management.system.utils.*;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.modules.system.domain.Dict;
import com.tplink.smb.common.data.management.system.modules.system.domain.DictDetail;
import com.tplink.smb.common.data.management.system.modules.system.repository.DictRepository;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDetailQueryCriteria;
import com.tplink.smb.common.data.management.system.utils.*;
import com.tplink.smb.common.data.management.system.modules.system.repository.DictDetailRepository;
import com.tplink.smb.common.data.management.system.modules.system.service.DictDetailService;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Service
@RequiredArgsConstructor
public class DictDetailServiceImpl implements DictDetailService {

  private final DictRepository dictRepository;
  private final DictDetailRepository dictDetailRepository;
  private final DictDetailMapper dictDetailMapper;
  private final RedisUtils redisUtils;

  @Override
  public PageResult<DictDetailDto> queryAll(DictDetailQueryCriteria criteria, Pageable pageable) {
    Page<DictDetail> page =
        dictDetailRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return PageUtil.toPage(page.map(dictDetailMapper::toDto));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void create(DictDetail resources) {
    dictDetailRepository.save(resources);
    // 清理缓存
    delCaches(resources);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(DictDetail resources) {
    DictDetail dictDetail =
        dictDetailRepository.findById(resources.getId()).orElseGet(DictDetail::new);
    ValidationUtil.isNull(dictDetail.getId(), "DictDetail", "id", resources.getId());
    resources.setId(dictDetail.getId());
    dictDetailRepository.save(resources);
    // 清理缓存
    delCaches(resources);
  }

  @Override
  public List<DictDetailDto> getDictByName(String name) {
    String key = CacheKey.DICT_NAME + name;
    List<DictDetail> dictDetails = redisUtils.getList(key, DictDetail.class);
    if (CollUtil.isEmpty(dictDetails)) {
      dictDetails = dictDetailRepository.findByDictName(name);
      redisUtils.set(key, dictDetails, 1, TimeUnit.DAYS);
    }
    return dictDetailMapper.toDto(dictDetails);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    DictDetail dictDetail = dictDetailRepository.findById(id).orElseGet(DictDetail::new);
    // 清理缓存
    delCaches(dictDetail);
    dictDetailRepository.deleteById(id);
  }

  public void delCaches(DictDetail dictDetail) {
    Dict dict = dictRepository.findById(dictDetail.getDict().getId()).orElseGet(Dict::new);
    redisUtils.del(CacheKey.DICT_NAME + dict.getName());
  }
}
