// DeviceInfoDto.java
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
package me.zhengjie.gen.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import io.swagger.annotations.ApiModelProperty;

/**
 * @website https://eladmin.vip
 * @description /
 * @author chen jiayuan
 * @date 2025-09-16
 **/
@Data
public class DeviceInfoDto implements Serializable {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "国家码，例如：JP")
    private String country;

    @ApiModelProperty(value = "设备型号，例如：ER7206")
    private String model;

    @ApiModelProperty(value = "设备版本，例如：1.0")
    private String modelVersion;

    @ApiModelProperty(value = "状态：0-下线，1-上线")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedAt;

    // 从 DeviceModelTemplatePO 展平的字段
    @ApiModelProperty(value = "模板硬件版本")
    private String hwVersion;

    @ApiModelProperty(value = "模板版本")
    private String version;

    @ApiModelProperty(value = "模板控制器版本")
    private String controllerVersion;

    @ApiModelProperty(value = "模板最小控制器版本")
    private String minControllerVersion;

    @ApiModelProperty(value = "模板不支持的控制器版本列表")
    private String notSupportControllerVersion;

    @ApiModelProperty(value = "模板类型")
    private String type;

    @ApiModelProperty(value = "模板模型类型")
    private String modelType;

    @ApiModelProperty(value = "是否支持IPPT")
    private Boolean ippt;

    @ApiModelProperty(value = "模板收养报文")
    private String adoptResp;

    // 从 ImageInfoPO 展平的字段
    @ApiModelProperty(value = "镜像名称")
    private String imageName;

    @ApiModelProperty(value = "镜像存储路径映射")
    private Map<String,String> imgBucketPathMap;

    // 在 DeviceInfoDto.java 中添加以下字段
    @ApiModelProperty(value = "web端小图路径")
    private String smallImgBucketPathForWeb;

    @ApiModelProperty(value = "web端热力图路径")
    private String heatmapImgBucketPathForWeb;

    @ApiModelProperty(value = "web端大图路径")
    private String bigImgBucketPathForWeb;

    @ApiModelProperty(value = "app端图路径")
    private String hdpiImgBucketPathForApp;

    @ApiModelProperty(value = "web端小图url")
    private String smallImgBucketUrlForWeb;

    @ApiModelProperty(value = "web端热力图url")
    private String heatmapImgBucketUrlForWeb;

    @ApiModelProperty(value = "web端大图url")
    private String bigImgBucketUrlForWeb;

    @ApiModelProperty(value = "app端图url")
    private String hdpiImgBucketUrlForApp;
}
