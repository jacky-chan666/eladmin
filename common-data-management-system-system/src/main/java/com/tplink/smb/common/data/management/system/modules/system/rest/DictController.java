/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.rest;

import com.tplink.smb.common.data.management.system.modules.system.domain.Dict;
import com.tplink.smb.common.data.management.system.modules.system.service.DictService;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDto;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.annotation.Log;
import com.tplink.smb.common.data.management.system.exception.BadRequestException;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典管理")
@RequestMapping("/api/dict")
public class DictController {

  private static final String ENTITY_NAME = "dict";
  private final DictService dictService;

  @ApiOperation("导出字典数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('dict:list')")
  public void exportDict(HttpServletResponse response, DictQueryCriteria criteria)
      throws IOException {
    dictService.download(dictService.queryAll(criteria), response);
  }

  @ApiOperation("查询字典")
  @GetMapping(value = "/all")
  @PreAuthorize("@el.check('dict:list')")
  public ResponseEntity<List<DictDto>> queryAllDict() {
    return new ResponseEntity<>(dictService.queryAll(new DictQueryCriteria()), HttpStatus.OK);
  }

  @ApiOperation("查询字典")
  @GetMapping
  @PreAuthorize("@el.check('dict:list')")
  public ResponseEntity<PageResult<DictDto>> queryDict(
      DictQueryCriteria resources, Pageable pageable) {
    return new ResponseEntity<>(dictService.queryAll(resources, pageable), HttpStatus.OK);
  }

  @Log("新增字典")
  @ApiOperation("新增字典")
  @PostMapping
  @PreAuthorize("@el.check('dict:add')")
  public ResponseEntity<Object> createDict(@Validated @RequestBody Dict resources) {
    if (resources.getId() != null) {
      throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
    }
    dictService.create(resources);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Log("修改字典")
  @ApiOperation("修改字典")
  @PutMapping
  @PreAuthorize("@el.check('dict:edit')")
  public ResponseEntity<Object> updateDict(
      @Validated(Dict.Update.class) @RequestBody Dict resources) {
    dictService.update(resources);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Log("删除字典")
  @ApiOperation("删除字典")
  @DeleteMapping
  @PreAuthorize("@el.check('dict:del')")
  public ResponseEntity<Object> deleteDict(@RequestBody Set<Long> ids) {
    dictService.delete(ids);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
