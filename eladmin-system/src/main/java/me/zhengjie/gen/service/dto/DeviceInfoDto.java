// 修改 DeviceInfoDto.java
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
 * @author chen jiayuan
 * @date 2025-09-16
 **/
@Data
public class DeviceInfoDto implements Serializable {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "设备型号，例如：ER7206")
    private String model;

    @ApiModelProperty(value = "设备版本，例如：1.0")
    private String modelVersion;

    @ApiModelProperty(value = "设备类型：NORMAL / PRO / COMBINED / PRO_FREE")
    private String modelType;

    @ApiModelProperty(value = "设备种类：gateway / switch / ap / olt / other")
    private String type;

    @ApiModelProperty(value = "硬件版本，例如：v1.2.3")
    private String hwVersion;

    @ApiModelProperty(value = "模版版本，例如：5.15.21.1")
    private String controllerVersion;

    @ApiModelProperty(value = "设备版本号，例如：1.0")
    private String version;

    @ApiModelProperty(value = "收养报文（JSON字符串），示例：{\"modelId\": \"123\"}")
    private String adoptResp;

    @ApiModelProperty(value = "状态：0-下线，1-上线")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedAt;
}
