// DeviceInfoMapper.java
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
package me.zhengjie.gen.service.mapstruct;

import me.zhengjie.gen.service.dto.DeviceInfoDto;
import me.zhengjie.base.BaseMapper;
import me.zhengjie.gen.domain.DeviceInfo;
import me.zhengjie.gen.domain.DeviceModelTemplatePO;
import me.zhengjie.gen.domain.ImageInfoPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Map;

/**
* @website https://eladmin.vip
* @author chen jiayuan
* @date 2025-09-16
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceInfoMapper extends BaseMapper<DeviceInfoDto, DeviceInfo> {

    /**
     * 将 DeviceInfo、DeviceModelTemplatePO 和 ImageInfoPO 映射为 DeviceInfoDto
     * @param deviceInfo 设备信息
     * @param template 设备模板信息
     * @param imageInfo 镜像信息
     * @return DeviceInfoDto
     */
    @Mapping(source = "deviceInfo.id", target = "id")
    @Mapping(source = "deviceInfo.model", target = "model")
    @Mapping(source = "deviceInfo.country", target = "country")
    @Mapping(source = "deviceInfo.modelVersion", target = "modelVersion")
    @Mapping(source = "deviceInfo.status", target = "status")
    @Mapping(source = "deviceInfo.createdAt", target = "createdAt")
    @Mapping(source = "deviceInfo.updatedAt", target = "updatedAt")
    // 映射模板信息
    @Mapping(source = "template.hwVersion", target = "hwVersion")
    @Mapping(source = "template.version", target = "version")
    @Mapping(source = "template.controllerVersion", target = "controllerVersion")
    @Mapping(source = "template.minControllerVersion", target = "minControllerVersion")
    @Mapping(source = "template.notSupportControllerVersion", target = "notSupportControllerVersion")
    @Mapping(source = "template.type", target = "type")
    @Mapping(source = "template.modelType", target = "modelType")
    @Mapping(source = "template.ippt", target = "ippt")
    @Mapping(source = "template.adoptResp", target = "adoptResp")
    // 映射镜像信息
    @Mapping(source = "imageInfo.imageName", target = "imageName")
    @Mapping(source = "imageInfo.minControllerVersion", target = "imageMinControllerVersion")
    @Mapping(source = "imageInfo.notSupportControllerVersion", target = "imageNotSupportControllerVersion")
    @Mapping(source = "imageInfo.imgBucketPathMap", target = "imgBucketPathMap")
    DeviceInfoDto toDto(DeviceInfo deviceInfo, DeviceModelTemplatePO template, ImageInfoPO imageInfo);
    // 添加一个辅助方法用于处理 imgBucketPathMap 的拆分映射
    default DeviceInfoDto toDtoWithImagePaths(DeviceInfo deviceInfo, DeviceModelTemplatePO template, ImageInfoPO imageInfo) {
        DeviceInfoDto dto = toDto(deviceInfo, template, imageInfo);

        // 拆分 imgBucketPathMap 为四个独立字段
        if (imageInfo != null && imageInfo.getImgBucketPathMap() != null) {
            Map<String, String> imgBucketPathMap = imageInfo.getImgBucketPathMap();
            dto.setSmallImgBucketPathForWeb(imgBucketPathMap.get("smallImgBucketPathForWeb"));
            dto.setHeatmapImgBucketPathForWeb(imgBucketPathMap.get("heatmapImgBucketPathForWeb"));
            dto.setBigImgBucketPathForWeb(imgBucketPathMap.get("bigImgBucketPathForWeb"));
            dto.setHdpiImgBucketPathForApp(imgBucketPathMap.get("hdpiImgBucketPathForApp"));
        }

        return dto;
    }

}
