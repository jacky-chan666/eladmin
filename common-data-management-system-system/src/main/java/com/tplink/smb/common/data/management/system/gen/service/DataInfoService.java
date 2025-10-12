/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface DataInfoService<T, D, C> {

  /**
   * 导出数据
   *
   * @param all 待导出的数据
   * @param response /
   * @throws IOException /
   */
  void download(List<T> all, HttpServletResponse response) throws IOException;

  Integer createFromJson(String dataDetail);

  void updateFromJson(String dataDetail, int deviceInfoId);

  void setDataStatus(Integer id, Integer status);

  /**
   * 解析设备信息详情JSON字符串
   *
   * @param dataDetails 设备信息详情JSON字符串
   * @return DeviceInfo对象
   */
  D parseDataDetails(String dataDetails);
}
