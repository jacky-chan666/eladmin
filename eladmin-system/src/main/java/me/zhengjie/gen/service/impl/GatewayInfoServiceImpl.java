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
package me.zhengjie.gen.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.zhengjie.gen.domain.DeviceInfo;
import me.zhengjie.gen.domain.GatewayInfo;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.gen.service.DataInfoService;
import me.zhengjie.gen.service.dto.DeviceInfoDto;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.gen.repository.GatewayInfoRepository;
import me.zhengjie.gen.service.GatewayInfoService;
import me.zhengjie.gen.service.dto.GatewayInfoDto;
import me.zhengjie.gen.service.dto.GatewayInfoQueryCriteria;
import me.zhengjie.gen.service.mapstruct.GatewayInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import me.zhengjie.utils.PageResult;

/**
 * @website https://eladmin.vip
 * @description 服务实现
 * @author Chen Jiayuan
 * @date 2025-09-21
 **/
@Service
@RequiredArgsConstructor
public class GatewayInfoServiceImpl implements GatewayInfoService {

    private final GatewayInfoRepository gatewayInfoRepository;
    private final GatewayInfoMapper gatewayInfoMapper;

    @Override
    public void download(List<GatewayInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (GatewayInfoDto gatewayInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("sb模型类型", gatewayInfo.getModel());
            map.put("sb模型版本", gatewayInfo.getModelVersion());
            map.put("sb设备名称", gatewayInfo.getSbname());
            map.put("sb设备类型", gatewayInfo.getType());
            map.put("制造商", gatewayInfo.getManufacturer());
            map.put("规格参数", gatewayInfo.getSpecifications());
            map.put("状态：0-下线，1-上线", gatewayInfo.getStatus());
            map.put("创建时间", gatewayInfo.getCreatedAt());
            map.put("更新时间", gatewayInfo.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createFromJson(String dataDetail) {
        GatewayInfo gatewayInfo = parseDataDetails(dataDetail);
        // 检查网关模型是否已存在
        if (gatewayInfoRepository.findByModel(gatewayInfo.getModel()) != null) {
            throw new RuntimeException("网关模型已存在");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        gatewayInfo.setCreatedAt(now);
        gatewayInfo.setUpdatedAt(now);

        // 设置默认状态为上线
        if (gatewayInfo.getStatus() == null) {
            gatewayInfo.setStatus(GatewayInfo.STATUS_ONLINE);
        }

        GatewayInfo save = gatewayInfoRepository.save(gatewayInfo);
        return save.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFromJson(String dataDetail,int deviceInfoId) {
        GatewayInfo gatewayInfo = parseDataDetails(dataDetail);
        GatewayInfo existing = gatewayInfoRepository.findById(gatewayInfo.getId())
                .orElseThrow(() -> new RuntimeException("网关信息不存在"));

        // 更新字段
        existing.setModel(gatewayInfo.getModel());
        existing.setModelVersion(gatewayInfo.getModelVersion());
        existing.setName(gatewayInfo.getName());
        existing.setType(gatewayInfo.getType());
        existing.setManufacturer(gatewayInfo.getManufacturer());
        existing.setSpecifications(gatewayInfo.getSpecifications());
        existing.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        gatewayInfoRepository.save(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDataStatus(Integer id, Integer status) {
        GatewayInfo gatewayInfo = gatewayInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("网关信息不存在"));

        gatewayInfo.setStatus(status);
        gatewayInfo.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        gatewayInfoRepository.save(gatewayInfo);
    }

    @Override
    public GatewayInfo parseDataDetails(String dataDetails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(dataDetails, GatewayInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("解析网关数据失败", e);
        }
    }
}