/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {

  @ApiModelProperty(value = "菜单标题")
  private String title;

  @ApiModelProperty(value = "菜单图标")
  private String icon;

  @ApiModelProperty(value = "缓存")
  private Boolean noCache;
}
