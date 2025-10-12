/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service.dto;

import com.tplink.smb.common.data.management.system.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@Setter
public class LocalStorageDto extends BaseDTO implements Serializable {

  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "真实文件名")
  private String realName;

  @ApiModelProperty(value = "文件名")
  private String name;

  @ApiModelProperty(value = "后缀")
  private String suffix;

  @ApiModelProperty(value = "文件类型")
  private String type;

  @ApiModelProperty(value = "文件大小")
  private String size;
}
