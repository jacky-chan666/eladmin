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

    @ApiModelProperty("设备详细信息（JSON格式字符串）")
    private String deviceDetail;
}
