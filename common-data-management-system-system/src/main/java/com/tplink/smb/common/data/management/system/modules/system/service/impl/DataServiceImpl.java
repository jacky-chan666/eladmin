/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.modules.system.domain.Dept;
import com.tplink.smb.common.data.management.system.modules.system.service.DataService;
import com.tplink.smb.common.data.management.system.modules.system.service.DeptService;
import com.tplink.smb.common.data.management.system.modules.system.service.RoleService;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.RoleSmallDto;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.UserDto;
import com.tplink.smb.common.data.management.system.utils.CacheKey;
import com.tplink.smb.common.data.management.system.utils.RedisUtils;
import com.tplink.smb.common.data.management.system.utils.enums.DataScopeEnum;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

  private final RedisUtils redisUtils;
  private final RoleService roleService;
  private final DeptService deptService;

  /**
   * 用户角色和用户部门改变时需清理缓存
   *
   * @param user /
   * @return /
   */
  @Override
  public List<Long> getDeptIds(UserDto user) {
    String key = CacheKey.DATA_USER + user.getId();
    List<Long> ids = redisUtils.getList(key, Long.class);
    if (CollUtil.isEmpty(ids)) {
      // 用于存储部门id
      Set<Long> deptIds = new HashSet<>();
      // 查询用户角色
      List<RoleSmallDto> roleSet = roleService.findByUsersId(user.getId());
      // 获取对应的部门ID
      for (RoleSmallDto role : roleSet) {
        DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
        switch (Objects.requireNonNull(dataScopeEnum)) {
          case THIS_LEVEL:
            deptIds.add(user.getDept().getId());
            break;
          case CUSTOMIZE:
            deptIds.addAll(getCustomize(deptIds, role));
            break;
          default:
            return new ArrayList<>();
        }
      }
      ids = new ArrayList<>(deptIds);
      redisUtils.set(key, ids, 1, TimeUnit.DAYS);
    }
    return new ArrayList<>(ids);
  }

  /**
   * 获取自定义的数据权限
   *
   * @param deptIds 部门ID
   * @param role 角色
   * @return 数据权限ID
   */
  public Set<Long> getCustomize(Set<Long> deptIds, RoleSmallDto role) {
    Set<Dept> depts = deptService.findByRoleId(role.getId());
    for (Dept dept : depts) {
      deptIds.add(dept.getId());
      List<Dept> deptChildren = deptService.findByPid(dept.getId());
      if (CollUtil.isNotEmpty(deptChildren)) {
        deptIds.addAll(deptService.getDeptChildren(deptChildren));
      }
    }
    return deptIds;
  }
}
