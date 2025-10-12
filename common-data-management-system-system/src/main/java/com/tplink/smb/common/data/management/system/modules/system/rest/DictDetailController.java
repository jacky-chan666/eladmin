/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.rest;

import com.tplink.smb.common.data.management.system.modules.system.domain.DictDetail;
import com.tplink.smb.common.data.management.system.modules.system.service.DictDetailService;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDetailDto;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDetailQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.annotation.Log;
import com.tplink.smb.common.data.management.system.exception.BadRequestException;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
public class DictDetailController {

  private static final String ENTITY_NAME = "dictDetail";
  private final DictDetailService dictDetailService;

  @ApiOperation("查询字典详情")
  @GetMapping
  public ResponseEntity<PageResult<DictDetailDto>> queryDictDetail(
      DictDetailQueryCriteria criteria,
      @PageableDefault(
              sort = {"dictSort"},
              direction = Sort.Direction.ASC)
          Pageable pageable) {
    return new ResponseEntity<>(dictDetailService.queryAll(criteria, pageable), HttpStatus.OK);
  }

  @ApiOperation("查询多个字典详情")
  @GetMapping(value = "/map")
  public ResponseEntity<Object> getDictDetailMaps(@RequestParam String dictName) {
    String[] names = dictName.split("[,，]");
    Map<String, List<DictDetailDto>> dictMap = new HashMap<>(16);
    for (String name : names) {
      dictMap.put(name, dictDetailService.getDictByName(name));
    }
    return new ResponseEntity<>(dictMap, HttpStatus.OK);
  }

  @Log("新增字典详情")
  @ApiOperation("新增字典详情")
  @PostMapping
  @PreAuthorize("@el.check('dict:add')")
  public ResponseEntity<Object> createDictDetail(@Validated @RequestBody DictDetail resources) {
    if (resources.getId() != null) {
      throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
    }
    dictDetailService.create(resources);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Log("修改字典详情")
  @ApiOperation("修改字典详情")
  @PutMapping
  @PreAuthorize("@el.check('dict:edit')")
  public ResponseEntity<Object> updateDictDetail(
      @Validated(DictDetail.Update.class) @RequestBody DictDetail resources) {
    dictDetailService.update(resources);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Log("删除字典详情")
  @ApiOperation("删除字典详情")
  @DeleteMapping(value = "/{id}")
  @PreAuthorize("@el.check('dict:del')")
  public ResponseEntity<Object> deleteDictDetail(@PathVariable Long id) {
    dictDetailService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
