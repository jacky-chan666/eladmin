// DeviceInfo.java (已有部分保持不变，新增关联关系)
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
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "country")
    @NotBlank
    @ApiModelProperty(value = "国家码，例如：JP")
    private String country;

    @Column(name = "model", unique = true)
    @NotBlank
    @ApiModelProperty(value = "设备型号，例如：ER7206")
    private String model;

    @Column(name = "model_version")
    @ApiModelProperty(value = "设备版本，例如：1.0")
    private String modelVersion;

    @Column(name = "model_template_id")
    @ApiModelProperty(value = "设备模板ID")
    private Integer modelTemplateId;

    @Column(name = "image_info_id")
    @ApiModelProperty(value = "镜像信息ID")
    private Integer imageInfoId;

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
