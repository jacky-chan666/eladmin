/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.tplink.smb.common.data.management.system.gen.utils.GenericJsonConverter;
import com.tplink.smb.common.data.management.system.gen.utils.StringListConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

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
@Entity
@Table(name = "device_model_template")
public class DeviceModelTemplatePO {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "model")
  private String model;

  @Column(name = "hw_version")
  private String hwVersion;

  @Column(name = "version")
  private String version;

  @Column(name = "controller_version")
  private String controllerVersion;

  @Column(name = "min_controller_version")
  private String minControllerVersion;

  @Column(name = "not_support_controller_version", columnDefinition = "JSON")
  @Convert(converter = StringListConverter.class)
  private List<String> notSupportControllerVersion;

  @Column(name = "type")
  private String type;

  @Column(name = "model_type")
  private String modelType;

  @Column(name = "ippt")
  private Boolean ippt;

  @Column(name = "model_version")
  private String modelVersion;

  @Column(name = "adopt_resp", columnDefinition = "JSON")
  @Convert(converter = GenericJsonConverter.class)
  private Map<String, Object> adoptResp; // 支持嵌套结构！
}
