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
    public PageResult<GatewayInfoDto> queryAll(GatewayInfoQueryCriteria criteria, Pageable pageable){
        Page<GatewayInfo> page = gatewayInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(gatewayInfoMapper::toDto));
    }

    @Override
    public List<GatewayInfoDto> queryAll(GatewayInfoQueryCriteria criteria){
        return gatewayInfoMapper.toDto(gatewayInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public GatewayInfoDto findById(Integer id) {
        GatewayInfo gatewayInfo = gatewayInfoRepository.findById(id).orElseGet(GatewayInfo::new);
        ValidationUtil.isNull(gatewayInfo.getId(),"GatewayInfo","id",id);
        return gatewayInfoMapper.toDto(gatewayInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(GatewayInfo resources) {
        if(gatewayInfoRepository.findByModel(resources.getModel()) != null){
            throw new EntityExistException(GatewayInfo.class,"model",resources.getModel());
        }
        gatewayInfoRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GatewayInfo resources) {
        GatewayInfo gatewayInfo = gatewayInfoRepository.findById(resources.getId()).orElseGet(GatewayInfo::new);
        ValidationUtil.isNull( gatewayInfo.getId(),"GatewayInfo","id",resources.getId());
        GatewayInfo gatewayInfo1 = null;
        gatewayInfo1 = gatewayInfoRepository.findByModel(resources.getModel());
        if(gatewayInfo1 != null && !gatewayInfo1.getId().equals(gatewayInfo.getId())){
            throw new EntityExistException(GatewayInfo.class,"model",resources.getModel());
        }
        gatewayInfo.copy(resources);
        gatewayInfoRepository.save(gatewayInfo);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            gatewayInfoRepository.deleteById(id);
        }
    }

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
    public void updateFromJson(String dataDetail) {
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

    @Override
    public List<GatewayInfoDto> getAllActiveDevices() {
        return gatewayInfoRepository.findByStatus(1).stream()
                .map(gatewayInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GatewayInfoDto> searchActiveDevices(String keyword) {
        return gatewayInfoRepository.findActiveDevicesByKeyword(keyword).stream()
                .map(gatewayInfoMapper::toDto)
                .collect(Collectors.toList());
    }

}