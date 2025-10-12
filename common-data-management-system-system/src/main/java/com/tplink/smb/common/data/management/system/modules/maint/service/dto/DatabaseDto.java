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
public class DatabaseDto extends BaseDTO implements Serializable {

  @ApiModelProperty(value = "ID")
  private String id;

  @ApiModelProperty(value = "数据库名称")
  private String name;

  @ApiModelProperty(value = "数据库连接地址")
  private String jdbcUrl;

  @ApiModelProperty(value = "数据库密码")
  private String pwd;

  @ApiModelProperty(value = "用户名")
  private String userName;
}
