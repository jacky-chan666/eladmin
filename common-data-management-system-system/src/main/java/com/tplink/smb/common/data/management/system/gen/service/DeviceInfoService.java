/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service;

import com.tplink.smb.common.data.management.system.gen.service.dto.DeviceInfoDto;
import com.tplink.smb.common.data.management.system.gen.service.dto.DeviceInfoQueryCriteria;
import com.tplink.smb.common.data.management.system.gen.domain.DeviceInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.tplink.smb.common.data.management.system.utils.PageResult;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface DeviceInfoService
    extends DataInfoService<DeviceInfoDto, DeviceInfo, DeviceInfoQueryCriteria> {

  /**
   * 导出数据
   *
   * @param all 待导出的数据
   * @param response /
   * @throws IOException /
   */
  void download(List<DeviceInfoDto> all, HttpServletResponse response) throws IOException;

  Integer createFromJson(String dataDetail);

  void updateFromJson(String dataDetail, int deviceInfoId);

  void setDataStatus(Integer id, Integer status);

  /**
   * 解析设备信息详情JSON字符串
   *
   * @param dataDetails 设备信息详情JSON字符串
   * @return DeviceInfo对象
   */
  DeviceInfo parseDataDetails(String dataDetails);

  /**
   * 根据ID查询设备详细信息（展平所有数据）
   *
   * @param id ID
   * @return DeviceInfoDto 包含展平的模板和镜像信息
   */
  DeviceInfoDto findDetailById(Integer id);

  /**
   * 查询数据分页（聚合详细信息）
   *
   * @param criteria 条件
   * @param pageable 分页参数
   * @return PageResult<DeviceInfoDto> 包含聚合信息的设备详情
   */
  PageResult<DeviceInfoDto> queryAllWithDetails(
      DeviceInfoQueryCriteria criteria, Pageable pageable);

  /**
   * 查询所有数据不分页（聚合详细信息）
   *
   * @param criteria 条件参数
   * @return List<DeviceInfoDto> 包含聚合信息的设备详情列表
   */
  List<DeviceInfoDto> queryAllWithDetails(DeviceInfoQueryCriteria criteria);

  // 在 DeviceInfoService.java 中添加以下方法声明
  /**
   * 同步设备信息到远程服务
   *
   * @param deviceId 设备ID
   * @param status 目标状态
   */
  void syncDeviceInfo(Integer deviceId, Integer status);
}
