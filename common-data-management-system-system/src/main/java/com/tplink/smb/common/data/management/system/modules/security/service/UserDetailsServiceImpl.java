/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.security.service;

import com.tplink.smb.common.data.management.system.exception.BadRequestException;
import com.tplink.smb.common.data.management.system.modules.security.service.dto.AuthorityDto;
import com.tplink.smb.common.data.management.system.modules.security.service.dto.JwtUserDto;
import com.tplink.smb.common.data.management.system.modules.system.service.DataService;
import com.tplink.smb.common.data.management.system.modules.system.service.RoleService;
import com.tplink.smb.common.data.management.system.modules.system.service.UserService;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserService userService;
  private final RoleService roleService;
  private final DataService dataService;
  private final UserCacheManager userCacheManager;

  @Override
  public JwtUserDto loadUserByUsername(String username) {
    JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
    if (jwtUserDto == null) {
      UserDto user = userService.getLoginData(username);
      if (user == null) {
        throw new BadRequestException("用户不存在");
      } else {
        if (!user.getEnabled()) {
          throw new BadRequestException("账号未激活！");
        }
        // 获取用户的权限
        List<AuthorityDto> authorities = roleService.buildPermissions(user);
        // 初始化JwtUserDto
        jwtUserDto = new JwtUserDto(user, dataService.getDeptIds(user), authorities);
        // 添加缓存数据
        userCacheManager.addUserCache(username, jwtUserDto);
      }
    }
    return jwtUserDto;
  }
}
