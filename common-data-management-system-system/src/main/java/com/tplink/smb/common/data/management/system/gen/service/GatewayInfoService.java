/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service;

import com.tplink.smb.common.data.management.system.gen.domain.GatewayInfo;
import com.tplink.smb.common.data.management.system.gen.service.dto.GatewayInfoDto;
import com.tplink.smb.common.data.management.system.gen.service.dto.GatewayInfoQueryCriteria;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface GatewayInfoService
    extends DataInfoService<GatewayInfoDto, GatewayInfo, GatewayInfoQueryCriteria> {

  /**
   * 导出数据
   *
   * @param all 待导出的数据
   * @param response /
   * @throws IOException /
   */
  void download(List<GatewayInfoDto> all, HttpServletResponse response) throws IOException;

  Integer createFromJson(String dataDetail);

  void updateFromJson(String dataDetail, int deviceInfoId);

  void setDataStatus(Integer id, Integer status);

  GatewayInfo parseDataDetails(String dataDetails);
}
