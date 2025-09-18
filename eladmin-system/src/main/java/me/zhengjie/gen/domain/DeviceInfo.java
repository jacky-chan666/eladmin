// E:/User/desktop/tplink/JavaProjects/eladmin/eladmin-system/src/main/java/me/zhengjie/gen/domain/DeviceInfo.java
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "model",nullable = false, unique = true)
    @NotBlank
    @ApiModelProperty(value = "模型类型")
    private String model;

    @Column(name = "`model_version`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "模型版本")
    private String modelVersion;

    @Column(name = "name")
    @ApiModelProperty(value = "设备名称")
    private String name;

    @Column(name = "type")
    @ApiModelProperty(value = "设备类型")
    private String type;

    @Column(name = "`manufacturer`")
    @ApiModelProperty(value = "制造商")
    private String manufacturer;

    @Column(name = "`specifications`")
    @ApiModelProperty(value = "规格参数")
    private String specifications;

    @Column(name = "status")
    @ApiModelProperty(value = "状态：0-下线，1-上线")
    private Integer status = 1; // 默认上线

    @Column(name = "`created_at`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @Column(name = "`updated_at`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedAt;

    public void copy(DeviceInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
