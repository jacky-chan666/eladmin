/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.domain.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ImageInfoActuateVO {
  /** 设备类型，如G611、ER605等 */
  @JsonProperty("model")
  private String model;

  /** 设备版本，如1.0、1.20、2.0等 */
  @JsonProperty("model_version")
  private String modelVersion;

  /** 设备唯一标识，如ER605 v1.0 */
  @JsonProperty("compound_model")
  private String compoundModel;

  /** 设备类型，目前主要用于Gateway */
  @JsonProperty("type")
  private String type;

  /** 图片名称 */
  @JsonProperty("img_name")
  private String imgName;

  /** 发布新机的数据版本号 */
  @JsonProperty("controller_version")
  private String controllerVersion;

  /** 发布新机最小支持的版本号 */
  @JsonProperty("min_controller_version")
  private String minControllerVersion;

  /** 发布新机的数据版本号 */
  @JsonProperty("not_support_controller_version")
  private List<String> notSupportControllerVersion;

  /** 存储图片桶路径 */
  @JsonProperty("image_bucket_path")
  private ImageBucketPathActuateVO imageBucketPathActuateVO;

  /** 发布状态 */
  @JsonProperty("status")
  private Integer status;
}
