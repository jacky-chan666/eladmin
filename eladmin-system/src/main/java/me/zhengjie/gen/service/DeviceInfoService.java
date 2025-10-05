// E:/User/desktop/tplink/JavaProjects/eladmin/eladmin-system/src/main/java/me/zhengjie/gen/service/DeviceInfoService.java
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
package me.zhengjie.gen.service;

import me.zhengjie.gen.domain.DeviceInfo;
import me.zhengjie.gen.service.dto.DeviceInfoDto;
import me.zhengjie.gen.service.dto.DeviceInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;

/**
 * @website https://eladmin.vip
 * @description 服务接口
 * @author chen jiayuan
 * @date 2025-09-16
 **/
public interface DeviceInfoService extends DataInfoService<DeviceInfoDto, DeviceInfo, DeviceInfoQueryCriteria>{

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return PageResult<DeviceInfoDto>
     */
    PageResult<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<DeviceInfoDto>
     */
    List<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return DeviceInfoDto
     */
    DeviceInfoDto findById(Integer id);

    /**
     * 创建
     * @param resources /
     */
    void create(DeviceInfo resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(DeviceInfo resources);

    /**
     * 多选删除
     * @param ids /
     */
    void deleteAll(Integer[] ids);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DeviceInfoDto> all, HttpServletResponse response) throws IOException;

    /**
     * 查询所有上线的设备
     * @return List<DeviceInfoDto>
     */
    List<DeviceInfoDto> getAllActiveDevices();

    /**
     * 根据关键字搜索上线的设备
     * @param keyword 关键字
     * @return List<DeviceInfoDto>
     */
    List<DeviceInfoDto> searchActiveDevices(String keyword);

    Integer createFromJson(String dataDetail);

    void updateFromJson(String dataDetail);

    void setDataStatus(Integer id, Integer status);

    /**
     * 解析设备信息详情JSON字符串
     * @param dataDetails 设备信息详情JSON字符串
     * @return DeviceInfo对象
     */
    DeviceInfo parseDataDetails(String dataDetails);

    /**
     * 根据ID查询设备详细信息（展平所有数据）
     * @param id ID
     * @return DeviceInfoDto 包含展平的模板和镜像信息
     */
    DeviceInfoDto findDetailById(Integer id);

    /**
     * 查询所有上线的设备详细信息（展平所有数据）
     * @return List<DeviceInfoDto> 包含展平的模板和镜像信息
     */
    List<DeviceInfoDto> getAllActiveDeviceDetails();

    /**
     * 根据关键字搜索上线的设备详细信息（展平所有数据）
     * @param keyword 关键字
     * @return List<DeviceInfoDto> 包含展平的模板和镜像信息
     */
    List<DeviceInfoDto> searchActiveDeviceDetails(String keyword);

    /**
     * 查询数据分页（聚合详细信息）
     * @param criteria 条件
     * @param pageable 分页参数
     * @return PageResult<DeviceInfoDto> 包含聚合信息的设备详情
     */
    PageResult<DeviceInfoDto> queryAllWithDetails(DeviceInfoQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页（聚合详细信息）
     * @param criteria 条件参数
     * @return List<DeviceInfoDto> 包含聚合信息的设备详情列表
     */
    List<DeviceInfoDto> queryAllWithDetails(DeviceInfoQueryCriteria criteria);

    // 在 DeviceInfoService.java 中添加以下方法声明
    /**
     * 同步设备信息到远程服务
     * @param deviceId 设备ID
     * @param status 目标状态
     */
    void syncDeviceInfo(Integer deviceId, Integer status);

}
