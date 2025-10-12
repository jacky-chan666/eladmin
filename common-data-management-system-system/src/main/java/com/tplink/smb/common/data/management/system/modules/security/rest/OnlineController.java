/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.security.rest;

import com.tplink.smb.common.data.management.system.modules.security.service.OnlineUserService;
import com.tplink.smb.common.data.management.system.modules.security.service.dto.OnlineUserDto;
import com.tplink.smb.common.data.management.system.utils.EncryptUtils;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {

  private final OnlineUserService onlineUserService;

  @ApiOperation("查询在线用户")
  @GetMapping
  @PreAuthorize("@el.check()")
  public ResponseEntity<PageResult<OnlineUserDto>> queryOnlineUser(
      String username, Pageable pageable) {
    return new ResponseEntity<>(onlineUserService.getAll(username, pageable), HttpStatus.OK);
  }

  @ApiOperation("导出数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check()")
  public void exportOnlineUser(HttpServletResponse response, String username) throws IOException {
    onlineUserService.download(onlineUserService.getAll(username), response);
  }

  @ApiOperation("踢出用户")
  @DeleteMapping
  @PreAuthorize("@el.check()")
  public ResponseEntity<Object> deleteOnlineUser(@RequestBody Set<String> keys) throws Exception {
    for (String token : keys) {
      // 解密Key
      token = EncryptUtils.desDecrypt(token);
      onlineUserService.logout(token);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
