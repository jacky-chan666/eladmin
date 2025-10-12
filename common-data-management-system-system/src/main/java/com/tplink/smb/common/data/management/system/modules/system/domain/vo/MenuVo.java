/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class MenuVo implements Serializable {

  @ApiModelProperty(value = "菜单名称")
  private String name;

  @ApiModelProperty(value = "路径")
  private String path;

  @ApiModelProperty(value = "隐藏状态")
  private Boolean hidden;

  @ApiModelProperty(value = "重定向")
  private String redirect;

  @ApiModelProperty(value = "组件")
  private String component;

  @ApiModelProperty(value = "总是显示")
  private Boolean alwaysShow;

  @ApiModelProperty(value = "元数据")
  private MenuMetaVo meta;

  @ApiModelProperty(value = "子路由")
  private List<MenuVo> children;
}
