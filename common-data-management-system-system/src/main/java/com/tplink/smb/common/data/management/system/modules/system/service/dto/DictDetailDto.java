/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

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
public class DictDetailDto extends BaseDTO implements Serializable {

  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "字典ID")
  private DictSmallDto dict;

  @ApiModelProperty(value = "字典标签")
  private String label;

  @ApiModelProperty(value = "字典值")
  private String value;

  @ApiModelProperty(value = "排序")
  private Integer dictSort;
}
