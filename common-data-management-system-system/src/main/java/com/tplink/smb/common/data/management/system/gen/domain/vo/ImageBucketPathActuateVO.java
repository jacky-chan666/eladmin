/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.domain.vo;

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
public class ImageBucketPathActuateVO {
  /** web端使用，小图 */
  @JsonProperty("small_img_bucket_path_for_web")
  private String smallImgBucketPathForWeb;

  /** web端使用，大图 */
  @JsonProperty("big_img_bucket_path_for_web")
  private String bigImgBucketPathForWeb;

  /** web端使用，热力图 */
  @JsonProperty("heatmap_img_bucket_path_for_web")
  private String heatmapImgBucketPathForWeb;

  /** app端的图片桶路径 */
  @JsonProperty("hdpi_img_bucket_path_for_app")
  private String hdpiImgBucketPathForApp;
}
