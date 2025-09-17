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
package me.chenjiayuan.gen.service.impl;

import me.chenjiayuan.gen.domain.DeviceInfo;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.chenjiayuan.gen.repository.DeviceInfoRepository;
import me.chenjiayuan.gen.service.DeviceInfoService;
import me.chenjiayuan.gen.service.dto.DeviceInfoDto;
import me.chenjiayuan.gen.service.dto.DeviceInfoQueryCriteria;
import me.chenjiayuan.gen.service.mapstruct.DeviceInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    @Override
    public PageResult<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria, Pageable pageable){
        Page<DeviceInfo> page = deviceInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(deviceInfoMapper::toDto));
    }

    @Override
    public List<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria){
        return deviceInfoMapper.toDto(deviceInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DeviceInfoDto findById(Integer id) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(id).orElseGet(DeviceInfo::new);
        ValidationUtil.isNull(deviceInfo.getId(),"DeviceInfo","id",id);
        return deviceInfoMapper.toDto(deviceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DeviceInfo resources) {
        deviceInfoRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceInfo resources) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(resources.getId()).orElseGet(DeviceInfo::new);
        ValidationUtil.isNull( deviceInfo.getId(),"DeviceInfo","id",resources.getId());
        deviceInfo.copy(resources);
        deviceInfoRepository.save(deviceInfo);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            deviceInfoRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DeviceInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeviceInfoDto deviceInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("模型类型", deviceInfo.getModel());
            map.put("模型版本", deviceInfo.getModelVersion());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}