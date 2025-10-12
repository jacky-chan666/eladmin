/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.rest;

import com.tplink.smb.common.data.management.system.gen.service.GatewayInfoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "网关信息")
@RequestMapping("/api/gatewayInfo")
public class GatewayInfoController {

  private final GatewayInfoService gatewayInfoService;
}
