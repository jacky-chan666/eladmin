/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.rest;

import com.tplink.smb.common.data.management.system.gen.service.DeviceInfoService;
import com.tplink.smb.common.data.management.system.gen.service.dto.DeviceInfoDto;
import com.tplink.smb.common.data.management.system.gen.service.dto.DeviceInfoQueryCriteria;
import com.tplink.smb.common.data.management.system.utils.PageResult;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
@Api(tags = "设备信息管理")
@RequestMapping("/api/deviceInfo")
public class DeviceInfoController {

  private final DeviceInfoService deviceInfoService;

  @ApiOperation("导出数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('deviceInfo:list')")
  public void exportDeviceInfo(HttpServletResponse response, DeviceInfoQueryCriteria criteria)
      throws IOException {
    deviceInfoService.download(deviceInfoService.queryAllWithDetails(criteria), response);
  }

  @GetMapping
  @ApiOperation("查询设备信息（聚合详细信息）")
  @PreAuthorize("@el.check('deviceInfo:list')")
  public ResponseEntity<PageResult<DeviceInfoDto>> queryDeviceInfo(
      DeviceInfoQueryCriteria criteria, Pageable pageable) {
    return new ResponseEntity<>(
        deviceInfoService.queryAllWithDetails(criteria, pageable), HttpStatus.OK);
  }
}
