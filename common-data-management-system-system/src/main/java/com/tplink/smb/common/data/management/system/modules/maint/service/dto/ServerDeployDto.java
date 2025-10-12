/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.tplink.smb.common.data.management.system.base.BaseDTO;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@Setter
public class ServerDeployDto extends BaseDTO implements Serializable {

  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "IP")
  private String ip;

  @ApiModelProperty(value = "端口")
  private Integer port;

  @ApiModelProperty(value = "账号")
  private String account;

  @ApiModelProperty(value = "密码")
  private String password;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServerDeployDto that = (ServerDeployDto) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
