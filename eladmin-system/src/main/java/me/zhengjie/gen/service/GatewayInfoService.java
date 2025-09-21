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
import me.zhengjie.gen.domain.GatewayInfo;
import me.zhengjie.gen.service.dto.GatewayInfoDto;
import me.zhengjie.gen.service.dto.GatewayInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author Chen Jiayuan
* @date 2025-09-21
**/
public interface GatewayInfoService extends DataInfoService<GatewayInfoDto, GatewayInfo, GatewayInfoQueryCriteria> {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<GatewayInfoDto> queryAll(GatewayInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<GatewayInfoDto>
    */
    List<GatewayInfoDto> queryAll(GatewayInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return GatewayInfoDto
     */
    GatewayInfoDto findById(Integer id);

    /**
    * 创建
    * @param resources /
    */
    void create(GatewayInfo resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(GatewayInfo resources);

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
    void download(List<GatewayInfoDto> all, HttpServletResponse response) throws IOException;

    void createFromJson(String dataDetail);

    void updateFromJson(String dataDetail);

    void setDataStatus(Integer id, Integer status);

    GatewayInfo parseDataDetails(String dataDetails);

}