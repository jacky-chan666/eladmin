/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.mapstruct;

import cn.hutool.json.JSONUtil;
import com.tplink.smb.common.data.management.system.base.BaseMapper;
import com.tplink.smb.common.data.management.system.gen.domain.DeviceInfo;
import com.tplink.smb.common.data.management.system.gen.domain.DeviceModelTemplatePO;
import com.tplink.smb.common.data.management.system.gen.domain.ImageInfoPO;
import com.tplink.smb.common.data.management.system.gen.service.dto.DeviceInfoDto;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceInfoMapper extends BaseMapper<DeviceInfoDto, DeviceInfo> {

  /** 自定义转换方法：List<String> -> CSV String */
  default String listToString(List<String> list) {
    return list != null && !list.isEmpty() ? String.join(",", list) : null;
  }

  /** 自定义转换方法：Map -> JSON String */
  default String mapToString(Map<String, Object> map) {
    return map != null ? JSONUtil.toJsonStr(map) : null;
  }

  /** 主映射方法 —— 使用上面两个转换器自动处理类型不匹配 */
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
  @Mapping(
      source = "template.notSupportControllerVersion",
      target = "notSupportControllerVersion") // List → String 自动调用 listToString
  @Mapping(source = "template.type", target = "type")
  @Mapping(source = "template.modelType", target = "modelType")
  @Mapping(source = "template.ippt", target = "ippt")
  @Mapping(source = "template.adoptResp", target = "adoptResp") // Map → String 自动调用 mapToString
  // 映射镜像信息
  @Mapping(source = "imageInfo.imageName", target = "imageName")
  @Mapping(source = "imageInfo.imgBucketPathMap", target = "imgBucketPathMap")
  DeviceInfoDto toDto(DeviceInfo deviceInfo, DeviceModelTemplatePO template, ImageInfoPO imageInfo);

  /** 辅助方法：完成主映射后，再拆分 imgBucketPathMap 字段 */
  default DeviceInfoDto toDtoWithImagePaths(
      DeviceInfo deviceInfo, DeviceModelTemplatePO template, ImageInfoPO imageInfo) {
    DeviceInfoDto dto = toDto(deviceInfo, template, imageInfo);

    // 拆分 imgBucketPathMap 提取四个具体路径字段
    if (imageInfo != null && imageInfo.getImgBucketPathMap() != null) {
      Map<String, String> imgBucketPathMap = imageInfo.getImgBucketPathMap();
      dto.setSmallImgBucketPathForWeb(imgBucketPathMap.get("small_img_bucket_path_for_web"));
      dto.setHeatmapImgBucketPathForWeb(imgBucketPathMap.get("heatmap_img_bucket_path_for_web"));
      dto.setBigImgBucketPathForWeb(imgBucketPathMap.get("big_img_bucket_path_for_web"));
      dto.setHdpiImgBucketPathForApp(imgBucketPathMap.get("hdpi_img_bucket_path_for_app"));
    }

    return dto;
  }
}
