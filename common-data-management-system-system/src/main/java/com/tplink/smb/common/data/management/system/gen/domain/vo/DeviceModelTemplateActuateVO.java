/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceModelTemplateActuateVO {
  /** 设备model，如ER605 */
  @JsonProperty("model")
  private String model;

  /** 组件协商报文中的hwVersion */
  @JsonProperty("hw_version")
  private String hwVersion;

  /** 组件协商报文header里的version */
  @JsonProperty("version")
  private String version;

  /** 设备类型 */
  @JsonProperty("type")
  private String type;

  /** 发布新机的数据版本号 */
  @JsonProperty("controller_version")
  private String controllerVersion;

  /** 发布新机最小支持的版本号 */
  @JsonProperty("min_controller_version")
  private String minControllerVersion;

  /** 发布新机的数据版本号 */
  @JsonProperty("not_support_controller_version")
  private List<String> notSupportControllerVersion;

  /** 是否为ippt模式 */
  @JsonProperty("ippt")
  private Boolean ippt;

  /** 设备的分类，Pro/Normal/一体机等 */
  @JsonProperty("model_type")
  private String modelType;

  /** 设备版本号，如1.0、2.0、1.20、2.20等 */
  @JsonProperty("model_version")
  private String modelVersion;

  /** 组件协商报文 */
  @JsonProperty("adopt_resp")
  private String adoptResp;

  /** 发布状态 */
  @JsonProperty("status")
  private Integer status;
}
