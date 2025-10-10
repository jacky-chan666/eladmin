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
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<GatewayInfoDto> all, HttpServletResponse response) throws IOException;

    Integer createFromJson(String dataDetail);

    void updateFromJson(String dataDetail,int deviceInfoId);

    void setDataStatus(Integer id, Integer status);

    GatewayInfo parseDataDetails(String dataDetails);

}