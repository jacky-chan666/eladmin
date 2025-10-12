/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.rest;

import com.tplink.smb.common.data.management.system.annotation.Log;
import com.tplink.smb.common.data.management.system.domain.LocalStorage;
import com.tplink.smb.common.data.management.system.exception.BadRequestException;
import com.tplink.smb.common.data.management.system.service.LocalStorageService;
import com.tplink.smb.common.data.management.system.service.dto.LocalStorageDto;
import com.tplink.smb.common.data.management.system.service.dto.LocalStorageQueryCriteria;
import com.tplink.smb.common.data.management.system.utils.FileUtil;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "工具：本地存储管理")
@RequestMapping("/api/localStorage")
public class LocalStorageController {

  private final LocalStorageService localStorageService;

  @GetMapping
  @ApiOperation("查询文件")
  @PreAuthorize("@el.check('storage:list')")
  public ResponseEntity<PageResult<LocalStorageDto>> queryFile(
      LocalStorageQueryCriteria criteria, Pageable pageable) {
    return new ResponseEntity<>(localStorageService.queryAll(criteria, pageable), HttpStatus.OK);
  }

  @ApiOperation("导出数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('storage:list')")
  public void exportFile(HttpServletResponse response, LocalStorageQueryCriteria criteria)
      throws IOException {
    localStorageService.download(localStorageService.queryAll(criteria), response);
  }

  @PostMapping
  @ApiOperation("上传文件")
  @PreAuthorize("@el.check('storage:add')")
  public ResponseEntity<Object> createFile(
      @RequestParam String name, @RequestParam("file") MultipartFile file) {
    localStorageService.create(name, file);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @ApiOperation("上传图片")
  @PostMapping("/pictures")
  public ResponseEntity<LocalStorage> uploadPicture(@RequestParam MultipartFile file) {
    // 判断文件是否为图片
    String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
    if (!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))) {
      throw new BadRequestException("只能上传图片");
    }
    LocalStorage localStorage = localStorageService.create(null, file);
    return new ResponseEntity<>(localStorage, HttpStatus.OK);
  }

  @PutMapping
  @Log("修改文件")
  @ApiOperation("修改文件")
  @PreAuthorize("@el.check('storage:edit')")
  public ResponseEntity<Object> updateFile(@Validated @RequestBody LocalStorage resources) {
    localStorageService.update(resources);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Log("删除文件")
  @DeleteMapping
  @ApiOperation("多选删除")
  public ResponseEntity<Object> deleteFile(@RequestBody Long[] ids) {
    localStorageService.deleteAll(ids);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
