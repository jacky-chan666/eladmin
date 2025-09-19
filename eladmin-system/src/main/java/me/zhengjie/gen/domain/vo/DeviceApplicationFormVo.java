/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */

package me.zhengjie.gen.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description of this file
 *
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/19
 */
@Data
public class DeviceApplicationFormVo {

    @ApiModelProperty("申请单ID,主键ID")
    private Integer id;

    @ApiModelProperty("申请单编号（系统生成，可选）")
    private String uuid;

    @ApiModelProperty("申请单标题")
    private String applicationTitle;

    @ApiModelProperty(value = "申请理由")
    private String applicationReason;

    @ApiModelProperty(value = "申请单类型：新增，修改，上线，下线")
    private Integer applicationType;

    @ApiModelProperty(value = "申请单数据类型：omada，vigi，adblocking")
    private Integer applicationDataType;

    @ApiModelProperty("研发接口人用户名")
    private String devContact;

    @ApiModelProperty("测试接口人用户名")
    private String testContact;

    @ApiModelProperty("研发组长用户名")
    private String devLeader;

    @ApiModelProperty("测试组长用户名")
    private String testLeader;

    @ApiModelProperty("申请人用户名（当前登录用户）")
    private String applicantUserName;

    @ApiModelProperty("设备详细信息")
    private DeviceDetail deviceDetail;


    @Data
    public static class DeviceDetail {

        @ApiModelProperty("设备型号，例如：ER7206")
        private String model;

        @ApiModelProperty("设备版本，例如：1.0")
        private String modelVersion;

        @ApiModelProperty("设备类型：NORMAL / PRO / COMBINED / PRO_FREE")
        private String modelType;

        @ApiModelProperty("设备种类：gateway / switch / ap / olt / other")
        private String type;

        @ApiModelProperty("硬件版本，例如：v1.2.3")
        private String hwVersion;

        @ApiModelProperty("模版版本，例如：5.15.21.1")
        private String controllerVersion;

        @ApiModelProperty("设备版本号，例如：1.0")
        private String version;

        @ApiModelProperty("收养报文（JSON字符串），示例：{\"modelId\": \"123\"}")
        private String adoptResp;
    }
}


 