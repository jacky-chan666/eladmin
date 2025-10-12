/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.security.service.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.UserDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@AllArgsConstructor
public class JwtUserDto implements UserDetails {

  @ApiModelProperty(value = "用户")
  private final UserDto user;

  @ApiModelProperty(value = "数据权限")
  private final List<Long> dataScopes;

  @ApiModelProperty(value = "角色权限")
  private final List<AuthorityDto> authorities;

  public Set<String> getRoles() {
    return authorities.stream().map(AuthorityDto::getAuthority).collect(Collectors.toSet());
  }

  @Override
  @JSONField(serialize = false)
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  @JSONField(serialize = false)
  public String getUsername() {
    return user.getUsername();
  }

  @JSONField(serialize = false)
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JSONField(serialize = false)
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JSONField(serialize = false)
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JSONField(serialize = false)
  public boolean isEnabled() {
    return user.getEnabled();
  }
}
