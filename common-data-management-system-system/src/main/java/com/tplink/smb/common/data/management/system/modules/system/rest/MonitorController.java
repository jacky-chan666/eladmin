/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.rest;

import com.tplink.smb.common.data.management.system.modules.system.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统-服务监控管理")
@RequestMapping("/api/monitor")
public class MonitorController {

  private final MonitorService serverService;

  @GetMapping
  @ApiOperation("查询服务监控")
  @PreAuthorize("@el.check('monitor:list')")
  public ResponseEntity<Object> queryMonitor() {
    return new ResponseEntity<>(serverService.getServers(), HttpStatus.OK);
  }
}
