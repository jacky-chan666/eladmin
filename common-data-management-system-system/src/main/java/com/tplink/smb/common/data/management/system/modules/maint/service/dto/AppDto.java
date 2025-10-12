/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.tplink.smb.common.data.management.system.base.BaseDTO;
import java.io.Serializable;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@Setter
public class AppDto extends BaseDTO implements Serializable {

  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "应用名称")
  private String name;

  @ApiModelProperty(value = "端口")
  private Integer port;

  @ApiModelProperty(value = "上传目录")
  private String uploadPath;

  @ApiModelProperty(value = "部署目录")
  private String deployPath;

  @ApiModelProperty(value = "备份目录")
  private String backupPath;

  @ApiModelProperty(value = "启动脚本")
  private String startScript;

  @ApiModelProperty(value = "部署脚本")
  private String deployScript;
}
