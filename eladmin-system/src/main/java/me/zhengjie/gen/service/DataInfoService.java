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
public interface DataInfoService <T,D,C>{

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<T> all, HttpServletResponse response) throws IOException;

    Integer createFromJson(String dataDetail);

    void updateFromJson(String dataDetail,int deviceInfoId);

    void setDataStatus(Integer id, Integer status);

    /**
     * 解析设备信息详情JSON字符串
     * @param dataDetails 设备信息详情JSON字符串
     * @return DeviceInfo对象
     */
    D parseDataDetails(String dataDetails);

} 