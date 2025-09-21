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

import me.zhengjie.annotation.Log;
import me.zhengjie.gen.domain.GatewayInfo;
import me.zhengjie.gen.service.GatewayInfoService;
import me.zhengjie.gen.service.dto.GatewayInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.gen.service.dto.GatewayInfoDto;

/**
* @website https://eladmin.vip
* @author Chen Jiayuan
* @date 2025-09-21
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "网关信息")
@RequestMapping("/api/gatewayInfo")
public class GatewayInfoController {

    private final GatewayInfoService gatewayInfoService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('gatewayInfo:list')")
    public void exportGatewayInfo(HttpServletResponse response, GatewayInfoQueryCriteria criteria) throws IOException {
        gatewayInfoService.download(gatewayInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询网关信息")
    @PreAuthorize("@el.check('gatewayInfo:list')")
    public ResponseEntity<PageResult<GatewayInfoDto>> queryGatewayInfo(GatewayInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(gatewayInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增网关信息")
    @ApiOperation("新增网关信息")
    @PreAuthorize("@el.check('gatewayInfo:add')")
    public ResponseEntity<Object> createGatewayInfo(@Validated @RequestBody GatewayInfo resources){
        gatewayInfoService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改网关信息")
    @ApiOperation("修改网关信息")
    @PreAuthorize("@el.check('gatewayInfo:edit')")
    public ResponseEntity<Object> updateGatewayInfo(@Validated @RequestBody GatewayInfo resources){
        gatewayInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除网关信息")
    @ApiOperation("删除网关信息")
    @PreAuthorize("@el.check('gatewayInfo:del')")
    public ResponseEntity<Object> deleteGatewayInfo(@ApiParam(value = "传ID数组[]") @RequestBody Integer[] ids) {
        gatewayInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}