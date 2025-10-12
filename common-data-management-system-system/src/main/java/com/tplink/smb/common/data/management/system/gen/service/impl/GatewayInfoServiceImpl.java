/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tplink.smb.common.data.management.system.gen.repository.GatewayInfoRepository;
import com.tplink.smb.common.data.management.system.gen.service.dto.GatewayInfoDto;
import com.tplink.smb.common.data.management.system.gen.service.mapstruct.GatewayInfoMapper;
import com.tplink.smb.common.data.management.system.gen.domain.GatewayInfo;
import com.tplink.smb.common.data.management.system.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.gen.service.GatewayInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Service
@RequiredArgsConstructor
public class GatewayInfoServiceImpl implements GatewayInfoService {

  private final GatewayInfoRepository gatewayInfoRepository;
  private final GatewayInfoMapper gatewayInfoMapper;

  @Override
  public void download(List<GatewayInfoDto> all, HttpServletResponse response) throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (GatewayInfoDto gatewayInfo : all) {
      Map<String, Object> map = new LinkedHashMap<>();
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
  public void updateFromJson(String dataDetail, int deviceInfoId) {
    GatewayInfo gatewayInfo = parseDataDetails(dataDetail);
    GatewayInfo existing =
        gatewayInfoRepository
            .findById(gatewayInfo.getId())
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
    GatewayInfo gatewayInfo =
        gatewayInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("网关信息不存在"));

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
