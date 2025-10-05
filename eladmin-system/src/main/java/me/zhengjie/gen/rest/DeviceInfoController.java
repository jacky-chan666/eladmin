// E:/User/desktop/tplink/JavaProjects/eladmin/eladmin-system/src/main/java/me/zhengjie/gen/rest/DeviceInfoController.java
/*
*  Copyright 2019-2025 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.gen.rest;

import me.zhengjie.gen.service.dto.DeviceInfoDto;
import me.zhengjie.annotation.Log;
import me.zhengjie.gen.domain.DeviceInfo;
import me.zhengjie.gen.service.DeviceInfoService;
import me.zhengjie.gen.service.dto.DeviceInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @author chen jiayuan
* @date 2025-09-16
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "设备信息管理")
@RequestMapping("/api/deviceInfo")
public class DeviceInfoController {

    private final DeviceInfoService deviceInfoService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deviceInfo:list')")
    public void exportDeviceInfo(HttpServletResponse response, DeviceInfoQueryCriteria criteria) throws IOException {
        deviceInfoService.download(deviceInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询设备信息（聚合详细信息）")
    @PreAuthorize("@el.check('deviceInfo:list')")
    public ResponseEntity<PageResult<DeviceInfoDto>> queryDeviceInfo(DeviceInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(deviceInfoService.queryAllWithDetails(criteria, pageable), HttpStatus.OK);
    }
}
