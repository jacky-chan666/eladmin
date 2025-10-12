/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.rest;

import com.tplink.smb.common.data.management.system.annotation.Log;
import com.tplink.smb.common.data.management.system.modules.maint.domain.Database;
import com.tplink.smb.common.data.management.system.modules.maint.service.DatabaseService;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.DatabaseDto;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.DatabaseQueryCriteria;
import com.tplink.smb.common.data.management.system.modules.maint.util.SqlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.exception.BadRequestException;
import com.tplink.smb.common.data.management.system.utils.FileUtil;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Api(tags = "运维：数据库管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/database")
public class DatabaseController {

  private final String fileSavePath = FileUtil.getTmpDirPath() + "/";
  private final DatabaseService databaseService;

  @ApiOperation("导出数据库数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('database:list')")
  public void exportDatabase(HttpServletResponse response, DatabaseQueryCriteria criteria)
      throws IOException {
    databaseService.download(databaseService.queryAll(criteria), response);
  }

  @ApiOperation(value = "查询数据库")
  @GetMapping
  @PreAuthorize("@el.check('database:list')")
  public ResponseEntity<PageResult<DatabaseDto>> queryDatabase(
      DatabaseQueryCriteria criteria, Pageable pageable) {
    return new ResponseEntity<>(databaseService.queryAll(criteria, pageable), HttpStatus.OK);
  }

  @Log("新增数据库")
  @ApiOperation(value = "新增数据库")
  @PostMapping
  @PreAuthorize("@el.check('database:add')")
  public ResponseEntity<Object> createDatabase(@Validated @RequestBody Database resources) {
    databaseService.create(resources);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Log("修改数据库")
  @ApiOperation(value = "修改数据库")
  @PutMapping
  @PreAuthorize("@el.check('database:edit')")
  public ResponseEntity<Object> updateDatabase(@Validated @RequestBody Database resources) {
    databaseService.update(resources);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Log("删除数据库")
  @ApiOperation(value = "删除数据库")
  @DeleteMapping
  @PreAuthorize("@el.check('database:del')")
  public ResponseEntity<Object> deleteDatabase(@RequestBody Set<String> ids) {
    databaseService.delete(ids);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Log("测试数据库链接")
  @ApiOperation(value = "测试数据库链接")
  @PostMapping("/testConnect")
  @PreAuthorize("@el.check('database:testConnect')")
  public ResponseEntity<Object> testConnect(@Validated @RequestBody Database resources) {
    return new ResponseEntity<>(databaseService.testConnection(resources), HttpStatus.CREATED);
  }

  @Log("执行SQL脚本")
  @ApiOperation(value = "执行SQL脚本")
  @PostMapping(value = "/upload")
  @PreAuthorize("@el.check('database:add')")
  public ResponseEntity<Object> uploadDatabase(
      @RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
    String id = request.getParameter("id");
    DatabaseDto database = databaseService.findById(id);
    String fileName;
    if (database != null) {
      fileName = FileUtil.verifyFilename(file.getOriginalFilename());
      File executeFile = new File(fileSavePath + fileName);
      FileUtil.del(executeFile);
      file.transferTo(executeFile);
      String result =
          SqlUtils.executeFile(
              database.getJdbcUrl(), database.getUserName(), database.getPwd(), executeFile);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } else {
      throw new BadRequestException("Database not exist");
    }
  }
}
