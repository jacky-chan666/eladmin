/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVo {

  @NotEmpty
  @ApiModelProperty(value = "收件人")
  private List<String> tos;

  @NotBlank
  @ApiModelProperty(value = "主题")
  private String subject;

  @NotBlank
  @ApiModelProperty(value = "内容")
  private String content;
}
