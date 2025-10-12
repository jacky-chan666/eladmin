/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.tplink.smb.common.data.management.system.base.BaseDTO;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@Setter
public class RoleDto extends BaseDTO implements Serializable {

  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "菜单")
  private Set<MenuDto> menus;

  @ApiModelProperty(value = "部门")
  private Set<DeptDto> depts;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "数据权限")
  private String dataScope;

  @ApiModelProperty(value = "级别")
  private Integer level;

  @ApiModelProperty(value = "描述")
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleDto roleDto = (RoleDto) o;
    return Objects.equals(id, roleDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
