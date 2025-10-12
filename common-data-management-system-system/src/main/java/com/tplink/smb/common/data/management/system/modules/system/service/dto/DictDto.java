/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.tplink.smb.common.data.management.system.base.BaseDTO;
import java.io.Serializable;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@Setter
public class DictDto extends BaseDTO implements Serializable {

  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "字典详情")
  private List<DictDetailDto> dictDetails;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "描述")
  private String description;
}
