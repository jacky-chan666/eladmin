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
    @ApiOperation("查询设备信息")
    @PreAuthorize("@el.check('deviceInfo:list')")
    public ResponseEntity<PageResult<DeviceInfoDto>> queryDeviceInfo(DeviceInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(deviceInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增设备信息")
    @ApiOperation("新增设备信息")
    @PreAuthorize("@el.check('deviceInfo:add')")
    public ResponseEntity<Object> createDeviceInfo(@Validated @RequestBody DeviceInfo resources){
        deviceInfoService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改设备信息")
    @ApiOperation("修改设备信息")
    @PreAuthorize("@el.check('deviceInfo:edit')")
    public ResponseEntity<Object> updateDeviceInfo(@Validated @RequestBody DeviceInfo resources){
        deviceInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除设备信息")
    @ApiOperation("删除设备信息")
    @PreAuthorize("@el.check('deviceInfo:del')")
    public ResponseEntity<Object> deleteDeviceInfo(@ApiParam(value = "传ID数组[]") @RequestBody Integer[] ids) {
        deviceInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/active")
    @ApiOperation("查询所有上线的设备信息")
    public ResponseEntity<List<DeviceInfoDto>> getAllActiveDevices() {
        return new ResponseEntity<>(deviceInfoService.getAllActiveDevices(), HttpStatus.OK);
    }

    @GetMapping("/active/search")
    @ApiOperation("搜索上线的设备信息")
    public ResponseEntity<List<DeviceInfoDto>> searchActiveDevices(@RequestParam String keyword) {
        return new ResponseEntity<>(deviceInfoService.searchActiveDevices(keyword), HttpStatus.OK);
    }
}
