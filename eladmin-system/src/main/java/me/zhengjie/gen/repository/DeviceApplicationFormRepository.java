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

import me.zhengjie.gen.domain.DeviceApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @website https://eladmin.vip
* @author Chen Jiayuan
* @date 2025-09-18
**/
public interface DeviceApplicationFormRepository extends JpaRepository<DeviceApplicationForm, Integer>, JpaSpecificationExecutor<DeviceApplicationForm> {
    /**
    * 根据 ApplicantId 查询
    * @param applicant_id /
    * @return /
    */
    DeviceApplicationForm findByApplicantId(String applicant_id);
    /**
    * 根据 ApplicationDataId 查询
    * @param application_data_id /
    * @return /
    */
    DeviceApplicationForm findByApplicationDataId(Integer application_data_id);
}