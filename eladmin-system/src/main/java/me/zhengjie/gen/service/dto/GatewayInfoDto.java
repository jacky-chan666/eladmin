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
import io.swagger.annotations.ApiModelProperty;

/**
* @website https://eladmin.vip
* @description /
* @author Chen Jiayuan
* @date 2025-09-21
**/
@Data
public class GatewayInfoDto implements Serializable {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "sb模型类型")
    private String model;

    @ApiModelProperty(value = "sb模型版本")
    private String modelVersion;

    @ApiModelProperty(value = "sb设备名称")
    private String sbname;

    @ApiModelProperty(value = "sb设备类型")
    private String type;

    @ApiModelProperty(value = "制造商")
    private String manufacturer;

    @ApiModelProperty(value = "规格参数")
    private String specifications;

    @ApiModelProperty(value = "状态：0-下线，1-上线")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedAt;
}