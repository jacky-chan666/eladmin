/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.security.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto implements GrantedAuthority {

  @ApiModelProperty(value = "角色名")
  private String authority;
}
