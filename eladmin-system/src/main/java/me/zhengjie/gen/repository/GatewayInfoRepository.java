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
package me.zhengjie.gen.repository;

import io.swagger.models.auth.In;
import me.zhengjie.gen.domain.DeviceInfo;
import me.zhengjie.gen.domain.GatewayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Collection;

/**
* @website https://eladmin.vip
* @author Chen Jiayuan
* @date 2025-09-21
**/
public interface GatewayInfoRepository extends JpaRepository<GatewayInfo, Integer>, JpaSpecificationExecutor<GatewayInfo> {
    /**
    * 根据 Model 查询
    * @param model /
    * @return /
    */
    GatewayInfo findByModel(String model);

    List<GatewayInfo> findByStatus(Integer i);

    @Query("SELECT d FROM GatewayInfo d WHERE d.status = 1 AND (d.model LIKE %:keyword% OR d.type LIKE %:keyword%)")
    List<GatewayInfo> findActiveDevicesByKeyword(@Param("keyword") String keyword);
}