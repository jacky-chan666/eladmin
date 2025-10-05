// 修改 DeviceInfo.java
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
package me.zhengjie.gen.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @website https://eladmin.vip
 * @description /
 * @author chen jiayuan
 * @date 2025-09-16
 **/
@Entity
@Data
@Table(name="device_info")
public class DeviceInfo implements Serializable {

    // 设备状态常量
    public static final Integer STATUS_OFFLINE = 0;  // 下线
    public static final Integer STATUS_ONLINE = 1;   // 上线

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "country")
    @NotBlank
    @ApiModelProperty(value = "国家码")
    private String country;

    @Column(name = "model",nullable = false, unique = true)
    @NotBlank
    @ApiModelProperty(value = "设备型号，例如：ER7206")
    private String model;

    @Column(name = "model_version",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "设备版本，例如：1.0")
    private String modelVersion;

    @Column(name = "model_type")
    @ApiModelProperty(value = "设备类型：NORMAL / PRO / COMBINED / PRO_FREE")
    private String modelType;

    @Column(name = "type")
    @ApiModelProperty(value = "设备种类：gateway / switch / ap / olt / other")
    private String type;

    @Column(name = "hw_version")
    @ApiModelProperty(value = "硬件版本，例如：v1.2.3")
    private String hwVersion;

    @Column(name = "controller_version")
    @ApiModelProperty(value = "模版版本，例如：5.15.21.1")
    private String controllerVersion;

    @Column(name = "version")
    @ApiModelProperty(value = "设备版本号，例如：1.0")
    private String version;

    @Column(name = "adopt_resp")
    @ApiModelProperty(value = "收养报文（JSON字符串），示例：{\"modelId\": \"123\"}")
    private String adoptResp;

    // ✅ 新增字段：最小支持控制器版本
    @Column(name = "min_controller_version", nullable = false)
    @NotBlank(message = "最小版本不能为空")
    @ApiModelProperty(value = "最小支持的控制器版本，例如：5.15.21.1")
    private String minControllerVersion;

    // ✅ 新增字段：不支持的控制器版本（多个用英文逗号分隔）
    @Column(name = "not_support_controller_version", nullable = false)
    @NotBlank(message = "不支持版本不能为空")
    @ApiModelProperty(value = "不支持的版本列表，多个用英文逗号分隔，例如：5.15.21.1, 5.1.2")
    private String notSupportControllerVersion;

    // ✅ 新增字段：IPPT（含义待定，保留原始命名）
    @Column(name = "ippt")
    @ApiModelProperty(value = "IPPT 标识（可选）")
    private String ippt;

    // ✅ 新增字段：设备 SPEC 规格（JSON 字符串）
    @Column(name = "specification", columnDefinition = "TEXT")
    @ApiModelProperty(value = "设备规格信息（JSON字符串），例如：{\"modelId\": \"123\"}")
    private String specification;

    // ✅ 新增字段：图片名称
    @Column(name = "image_name")
    @ApiModelProperty(value = "设备对应图片名称")
    private String imageName;

    // ✅ 新增字段：Web 端小图路径（OSS/Bucket 路径）
    @Column(name = "small_img_bucket_path_for_web")
    @ApiModelProperty(value = "Web端小图在对象存储中的路径")
    private String smallImgBucketPathForWeb;

    // ✅ 新增字段：Web 端热力图路径
    @Column(name = "heatmap_img_bucket_path_for_web")
    @ApiModelProperty(value = "Web端热力图在对象存储中的路径")
    private String heatmapImgBucketPathForWeb;

    // ✅ 新增字段：Web 端大图路径
    @Column(name = "big_img_bucket_path_for_web")
    @ApiModelProperty(value = "Web端大图在对象存储中的路径")
    private String bigImgBucketPathForWeb;

    // ✅ 新增字段：App 端高清图路径
    @Column(name = "hdpi_img_bucket_path_for_app")
    @ApiModelProperty(value = "App端高清图在对象存储中的路径")
    private String hdpiImgBucketPathForApp;

    @Column(name = "status")
    @ApiModelProperty(value = "状态：0-下线，1-上线")
    private Integer status = 1; // 默认上线

    @Column(name = "created_at")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedAt;

    public void copy(DeviceInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
