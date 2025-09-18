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
import java.util.List;
import me.zhengjie.annotation.Query;
import io.swagger.annotations.ApiModelProperty;

/**
* @website https://eladmin.vip
* @author Chen Jiayuan
* @date 2025-09-18
**/
@Data
public class DeviceApplicationFormQueryCriteria{

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "申请单UUID")
    private String applicantId;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "申请单类型：新增，修改，上线，下线")
    private Integer applicationType;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "申请单数据类型：omada，vigi，adblocking")
    private Integer applicationDataType;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "申请单标题")
    private String applicationTitle;
}