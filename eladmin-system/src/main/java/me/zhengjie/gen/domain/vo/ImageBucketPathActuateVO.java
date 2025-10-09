/*
 * Copyright (c) 2024, TP-Link Corporation Limited. All rights reserved.
 */

package me.zhengjie.gen.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zhou Zhuoran
 * @version 1.0
 * @since 2024/12/9
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageBucketPathActuateVO {
    /**
     * web端使用，小图
     */
    @JsonProperty("small_img_bucket_path_for_web")
    private String smallImgBucketPathForWeb;

    /**
     * web端使用，大图
     */
    @JsonProperty("big_img_bucket_path_for_web")
    private String bigImgBucketPathForWeb;

    /**
     * web端使用，热力图
     */
    @JsonProperty("heatmap_img_bucket_path_for_web")
    private String heatmapImgBucketPathForWeb;

    /**
     * app端的图片桶路径
     */
    @JsonProperty("hdpi_img_bucket_path_for_app")
    private String hdpiImgBucketPathForApp;
}
 