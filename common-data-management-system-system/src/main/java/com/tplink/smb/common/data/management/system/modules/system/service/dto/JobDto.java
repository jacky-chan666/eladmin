/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class JobDto extends BaseDTO implements Serializable {

  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "岗位排序")
  private Integer jobSort;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "是否启用")
  private Boolean enabled;

  public JobDto(String name, Boolean enabled) {
    this.name = name;
    this.enabled = enabled;
  }
}
