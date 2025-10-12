/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.chenjiayuan.gen.service;

import me.chenjiayuan.gen.domain.DeviceInfo;
import me.chenjiayuan.gen.service.dto.DeviceInfoDto;
import me.chenjiayuan.gen.service.dto.DeviceInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.tplink.smb.common.data.management.system.utils.PageResult;

/**
 * @website https://eladmin.vip
 * @description 服务接口
 * @author chen jiayuan
 * @date 2025-09-16
 */
public interface DeviceInfoService {

  /**
   * 查询数据分页
   *
   * @param criteria 条件
   * @param pageable 分页参数
   * @return Map<String,Object>
   */
  PageResult<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria, Pageable pageable);

  /**
   * 查询所有数据不分页
   *
   * @param criteria 条件参数
   * @return List<DeviceInfoDto>
   */
  List<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria);

  /**
   * 根据ID查询
   *
   * @param id ID
   * @return DeviceInfoDto
   */
  DeviceInfoDto findById(Integer id);

  /**
   * 创建
   *
   * @param resources /
   */
  void create(DeviceInfo resources);

  /**
   * 编辑
   *
   * @param resources /
   */
  void update(DeviceInfo resources);

  /**
   * 多选删除
   *
   * @param ids /
   */
  void deleteAll(Integer[] ids);

  /**
   * 导出数据
   *
   * @param all 待导出的数据
   * @param response /
   * @throws IOException /
   */
  void download(List<DeviceInfoDto> all, HttpServletResponse response) throws IOException;
}
