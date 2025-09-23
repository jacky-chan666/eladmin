// E:/User/desktop/tplink/JavaProjects/eladmin/eladmin-system/src/main/java/me/zhengjie/gen/service/impl/DeviceInfoServiceImpl.java
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
import me.zhengjie.gen.domain.ApplicationForm;
import me.zhengjie.gen.domain.DeviceInfo;
import me.zhengjie.gen.domain.GatewayInfo;
import me.zhengjie.gen.service.DataInfoService;
import me.zhengjie.gen.service.dto.DeviceInfoDto;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.gen.repository.DeviceInfoRepository;
import me.zhengjie.gen.service.DeviceInfoService;
import me.zhengjie.gen.service.dto.DeviceInfoQueryCriteria;
import me.zhengjie.gen.service.mapstruct.DeviceInfoMapper;
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
 * @author chen jiayuan
 * @date 2025-09-16
 **/
@Service
@RequiredArgsConstructor
public class DeviceInfoServiceImpl implements DeviceInfoService {

    private final DeviceInfoRepository deviceInfoRepository;
    private final DeviceInfoMapper deviceInfoMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PageResult<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria, Pageable pageable) {
        Page<DeviceInfo> page = deviceInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(deviceInfoMapper::toDto));
    }

    @Override
    public List<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria) {
        return deviceInfoMapper.toDto(deviceInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public DeviceInfoDto findById(Integer id) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(id).orElseGet(DeviceInfo::new);
        ValidationUtil.isNull(deviceInfo.getId(), "DeviceInfo", "id", id);
        return deviceInfoMapper.toDto(deviceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DeviceInfo resources) {
        if (deviceInfoRepository.existsByModel(resources.getModel())) {
            throw new RuntimeException("设备型号已存在");
        }
        deviceInfoRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceInfo resources) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(resources.getId()).orElseGet(DeviceInfo::new);
        ValidationUtil.isNull(deviceInfo.getId(), "DeviceInfo", "id", resources.getId());
        deviceInfo.copy(resources);
        deviceInfoRepository.save(deviceInfo);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            deviceInfoRepository.deleteById(id);
        }
    }

    // 修改 DeviceInfoServiceImpl.java 中的 download 方法
    @Override
    public void download(List<DeviceInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeviceInfoDto deviceInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("设备型号", deviceInfo.getModel());
            map.put("设备版本", deviceInfo.getModelVersion());
            map.put("设备类型", deviceInfo.getModelType());
            map.put("设备种类", deviceInfo.getType());
            map.put("硬件版本", deviceInfo.getHwVersion());
            map.put("模版版本", deviceInfo.getControllerVersion());
            map.put("设备版本号", deviceInfo.getVersion());
            map.put("收养报文", deviceInfo.getAdoptResp());
            map.put("状态", deviceInfo.getStatus() == 1 ? "上线" : "下线");
            map.put("创建时间", deviceInfo.getCreatedAt());
            map.put("更新时间", deviceInfo.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public List<DeviceInfoDto> getAllActiveDevices() {
        return deviceInfoRepository.findByStatus(1).stream()
                .map(deviceInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceInfoDto> searchActiveDevices(String keyword) {
        return deviceInfoRepository.findActiveDevicesByKeyword(keyword).stream()
                .map(deviceInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createFromJson(String dataDetails) {
        DeviceInfo deviceInfo = parseDataDetails(dataDetails);
        // TODO 检查设备型号是否已存在,需要在前后端加上校验；前端输入的时候就应该校验，而不是审批通过时校验。
//        if (deviceInfoRepository.existsByModel(deviceInfo.getModel())) {
//            throw new RuntimeException("设备型号已存在");
//        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        deviceInfo.setCreatedAt(now);
        deviceInfo.setUpdatedAt(now);
        deviceInfo.setStatus(DeviceInfo.STATUS_ONLINE);

        DeviceInfo save = deviceInfoRepository.save(deviceInfo);
        return save.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFromJson(String dataDetails) {
        DeviceInfo deviceInfo = parseDataDetails(dataDetails);
        DeviceInfo existingDevice = deviceInfoRepository.findById(deviceInfo.getId())
                .orElseThrow(() -> new RuntimeException("设备不存在"));

        existingDevice.setModel(deviceInfo.getModel());
        existingDevice.setModelVersion(deviceInfo.getModelVersion());
        existingDevice.setModelType(deviceInfo.getModelType());
        existingDevice.setType(deviceInfo.getType());
        existingDevice.setHwVersion(deviceInfo.getHwVersion());
        existingDevice.setControllerVersion(deviceInfo.getControllerVersion());
        existingDevice.setVersion(deviceInfo.getVersion());
        existingDevice.setAdoptResp(deviceInfo.getAdoptResp());
        existingDevice.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        deviceInfoRepository.save(existingDevice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDataStatus(Integer id, Integer status) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备不存在"));

        deviceInfo.setStatus(status);
        deviceInfoRepository.save(deviceInfo);
    }

    @Override
    public DeviceInfo parseDataDetails(String dataDetails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(dataDetails, DeviceInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("解析网关数据失败", e);
        }
    }
}

