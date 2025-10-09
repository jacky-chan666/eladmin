// E:/User/desktop/tplink/JavaProjects/eladmin/eladmin-system/src/main/java/me/zhengjie/gen/service/impl/DeviceInfoServiceImpl.java
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
package me.zhengjie.gen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.zhengjie.gen.domain.*;
import me.zhengjie.gen.domain.vo.DeviceModelTemplateActuateVO;
import me.zhengjie.gen.domain.vo.ImageBucketPathActuateVO;
import me.zhengjie.gen.domain.vo.ImageInfoActuateVO;
import me.zhengjie.gen.repository.DeviceModelTemplateRepository;
import me.zhengjie.gen.repository.ImageInfoRepository;
import me.zhengjie.gen.service.DataInfoService;
import me.zhengjie.gen.service.dto.DeviceInfoDto;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.gen.repository.DeviceInfoRepository;
import me.zhengjie.gen.service.DeviceInfoService;
import me.zhengjie.gen.service.dto.DeviceInfoQueryCriteria;
import me.zhengjie.gen.service.mapstruct.DeviceInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

import java.sql.Timestamp;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.zhengjie.utils.PageResult;
import org.springframework.web.client.RestTemplate;
// 在 ApplicationFormServiceImpl.java 中添加
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @website https://eladmin.vip
 * @description 服务实现
 * @author chen jiayuan
 * @date 2025-09-16
 **/
@Service
@RequiredArgsConstructor
public class DeviceInfoServiceImpl implements DeviceInfoService {

    private final DeviceInfoRepository deviceInfoRepository;
    private final DeviceModelTemplateRepository deviceModelTemplateRepository; // 新增
    private final ImageInfoRepository imageInfoRepository; // 新增
    private final DeviceInfoMapper deviceInfoMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 分隔符正则：支持中英文逗号、分号、竖线
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[,，;|]+");

    public static List<String> parseVersionsToList(Object input) {
        if (input == null) {
            return Collections.emptyList();
        }

        String str = input.toString().trim();

        try {
            List<String> result = new ArrayList<>();

            if (JSONUtil.isJsonArray(str)) {
                // 情况1: 已是合法 JSON 数组，例如 ["v1","v2"]
                JSONArray array = JSONUtil.parseArray(str);
                result = array.toList(String.class);

            } else if (str.startsWith("[") && str.endsWith("]")) {
                // 情况2: 伪数组格式，例如 ['v1','v2'] 或 ["v1","v2"]
                result = parseQuasiArray(str);

            } else if (StrUtil.containsAny(str, ',', '，', ';', '|')) {
                // 情况3: 分隔符字符串，例如 "v1,v2" 或 "v1；v2"
                result = Arrays.stream(SPLIT_PATTERN.split(str))
                        .map(String::trim)
                        .filter(StrUtil::isNotBlank)
                        .collect(Collectors.toList());

            } else if (StrUtil.isNotBlank(str)) {
                // 情况4: 单个非空值
                result.add(str.trim());
            }

            // 清洗：去空、去重、转小写可选（根据业务决定）
            List<String> cleaned = result.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(StrUtil::isNotBlank)
                    .distinct()
                    .collect(Collectors.toList());

            return Collections.unmodifiableList(cleaned); // 返回不可变列表

        } catch (Exception e) {
            // 解析失败时返回空列表（安全兜底）
            return Collections.emptyList();
        }
    }

    /**
     * 解析类似 ['a','b'] 或 ["a","b"] 的伪数组字符串
     */
    private static List<String> parseQuasiArray(String input) {
        return Arrays.stream(input
                        .replaceAll("[\\[\\]\"]", "")   // 去除 [, ], "
                        .replaceAll("'", "")           // 去除单引号
                        .split("[,，;|]+")              // 按分隔符拆分
                )
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
    }


    @Override
    public PageResult<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria, Pageable pageable) {
        Page<DeviceInfo> page = deviceInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(deviceInfoMapper::toDto));
    }

    @Override
    public List<DeviceInfoDto> queryAll(DeviceInfoQueryCriteria criteria) {
        return deviceInfoMapper.toDto(deviceInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public DeviceInfoDto findById(Integer id) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(id).orElseGet(DeviceInfo::new);
        ValidationUtil.isNull(deviceInfo.getId(), "DeviceInfo", "id", id);
        return deviceInfoMapper.toDto(deviceInfo);
    }


    // 修改 DeviceInfoServiceImpl.java 中的 download 方法
    @Override
    public void download(List<DeviceInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeviceInfoDto deviceInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("设备型号", deviceInfo.getModel());
            map.put("设备版本", deviceInfo.getModelVersion());
            map.put("设备类型", deviceInfo.getModelType());
            map.put("设备种类", deviceInfo.getType());
            map.put("硬件版本", deviceInfo.getHwVersion());
            map.put("模版版本", deviceInfo.getControllerVersion());
            map.put("设备版本号", deviceInfo.getVersion());
            map.put("收养报文", deviceInfo.getAdoptResp());
            map.put("状态", deviceInfo.getStatus() == 1 ? "上线" : "下线");
            map.put("创建时间", deviceInfo.getCreatedAt());
            map.put("更新时间", deviceInfo.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public List<DeviceInfoDto> getAllActiveDevices() {
        return deviceInfoRepository.findByStatus(1).stream()
                .map(deviceInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceInfoDto> searchActiveDevices(String keyword) {
        return deviceInfoRepository.findActiveDevicesByKeyword(keyword).stream()
                .map(deviceInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createFromJson(String dataDetails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // 先解析为Map方便提取不同部分的数据
            Map<String, Object> dataMap = objectMapper.readValue(dataDetails, Map.class);

            // 提取基本信息创建DeviceInfo
            DeviceInfo deviceInfo = new DeviceInfo();
            if (dataMap.containsKey("model")) {
                deviceInfo.setModel((String) dataMap.get("model"));
            }
            if (dataMap.containsKey("modelVersion")) {
                deviceInfo.setModelVersion((String) dataMap.get("modelVersion"));
            }
            // TODO 其他基本信息...
            if (dataMap.containsKey("country")) {
                deviceInfo.setCountry((String) dataMap.get("country"));

            }
            // 检查设备型号是否已存在
            if (deviceInfoRepository.existsByModel(deviceInfo.getModel())) {
                throw new RuntimeException("设备型号已存在");
            }

            Timestamp now = new Timestamp(System.currentTimeMillis());
            deviceInfo.setCreatedAt(now);
            deviceInfo.setUpdatedAt(now);
            // deviceInfo.setStatus(DeviceInfo.STATUS_ONLINE);

            // TODO 数据校验前端也要做！ 包括adot_resp，和 not support version

            // 假设 dataMap 是从某处获取的原始数据，其中 adoptResp 是一个 JSON 字符串
            String adoptRespStr = (String) dataMap.get("adoptResp");
            Map<String, Object> adoptRespMap = null;
            if (adoptRespStr != null && !adoptRespStr.trim().isEmpty()) {
                try {
                    adoptRespMap = objectMapper.readValue(adoptRespStr, Map.class);
                } catch (IOException e) {
                    throw new IllegalArgumentException("解析 adoptResp JSON 失败", e);
                }
            }

            // 提取并创建DeviceModelTemplatePO
            DeviceModelTemplatePO modelTemplate = DeviceModelTemplatePO.builder()
                    .model(deviceInfo.getModel())
                    .modelVersion(deviceInfo.getModelVersion())
                    .hwVersion((String)dataMap.get("hwVersion"))
                    .version((String)dataMap.get("version"))
                    .controllerVersion((String)dataMap.get("controllerVersion"))
                    .minControllerVersion((String)dataMap.get("minControllerVersion"))
                    .notSupportControllerVersion(parseVersionsToList((String)dataMap.get("notSupportControllerVersion")))
                    .type((String)dataMap.get("type"))
                    .modelType((String)dataMap.get("modelType"))
                    .ippt(Boolean.parseBoolean((String)dataMap.get("ippt")))
                    .adoptResp(adoptRespMap)
                    .build();

            // 保存模板信息
            DeviceModelTemplatePO savedTemplate = deviceModelTemplateRepository.save(modelTemplate);
            deviceInfo.setModelTemplateId(savedTemplate.getId().intValue()); // 转换为 Integer

            // 构建 imgBucketPathMap
            Map<String, String> imgBucketPathMap = new HashMap<>();
            if (dataMap.containsKey("smallImgBucketPathForWeb")) {
                imgBucketPathMap.put("small_img_bucket_path_for_web", (String)dataMap.get("smallImgBucketPathForWeb"));
            }
            if (dataMap.containsKey("heatmapImgBucketPathForWeb")) {
                imgBucketPathMap.put("heatmap_img_bucket_path_for_web", (String)dataMap.get("heatmapImgBucketPathForWeb"));
            }
            if (dataMap.containsKey("bigImgBucketPathForWeb")) {
                imgBucketPathMap.put("big_img_bucket_path_for_web", (String)dataMap.get("bigImgBucketPathForWeb"));
            }
            if (dataMap.containsKey("hdpiImgBucketPathForApp")) {
                imgBucketPathMap.put("hdpi_img_bucket_path_for_app", (String)dataMap.get("hdpiImgBucketPathForApp"));
            }


            // 提取并创建ImageInfoPO
            ImageInfoPO imageInfo = ImageInfoPO.builder()
                    .compoundModel(deviceInfo.getModel()+" v"+deviceInfo.getModelVersion())
                    .model(deviceInfo.getModel())
                    .modelVersion(deviceInfo.getModelVersion())
                    .type((String)dataMap.get("type"))
                    .controllerVersion((String)dataMap.get("controllerVersion"))
                    .minControllerVersion((String)dataMap.get("minControllerVersion"))
                    .notSupportControllerVersion(parseVersionsToList((String)dataMap.get("notSupportControllerVersion")))
                    .imageName((String)dataMap.get("imageName"))
                    .imgBucketPathMap(imgBucketPathMap)
                    .build();

            // 保存镜像信息
            ImageInfoPO savedImageInfo = imageInfoRepository.save(imageInfo);
            deviceInfo.setImageInfoId(savedImageInfo.getId().intValue()); // 转换为 Integer
            DeviceInfo save = deviceInfoRepository.save(deviceInfo);
            return save.getId();
        } catch (Exception e) {
            throw new RuntimeException("解析设备数据失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFromJson(String dataDetails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // 解析为Map方便提取不同部分的数据
            Map<String, Object> dataMap = objectMapper.readValue(dataDetails, Map.class);

            // 获取设备基本信息
            Integer deviceId = (Integer) dataMap.get("id");
            DeviceInfo existingDevice = deviceInfoRepository.findById(deviceId)
                    .orElseThrow(() -> new RuntimeException("设备不存在"));

            // 更新基本信息
            if (dataMap.containsKey("model")) {
                existingDevice.setModel((String) dataMap.get("model"));
            }
            if (dataMap.containsKey("modelVersion")) {
                existingDevice.setModelVersion((String) dataMap.get("modelVersion"));
            }
            // 更新其他基本信息...

            // 更新模板信息
            if (existingDevice.getModelTemplateId() != null) {
                DeviceModelTemplatePO existingTemplate = deviceModelTemplateRepository
                        .findById(existingDevice.getModelTemplateId())
                        .orElse(null);
                if (existingTemplate != null) {
                    if (dataMap.containsKey("hwVersion")) {
                        existingTemplate.setHwVersion((String)dataMap.get("hwVersion"));
                    }
                    if (dataMap.containsKey("version")) {
                        existingTemplate.setVersion((String)dataMap.get("version"));
                    }
                    if (dataMap.containsKey("controllerVersion")) {
                        existingTemplate.setControllerVersion((String)dataMap.get("controllerVersion"));
                    }
                    if (dataMap.containsKey("minControllerVersion")) {
                        existingTemplate.setMinControllerVersion((String)dataMap.get("minControllerVersion"));
                    }
                    if (dataMap.containsKey("notSupportControllerVersion")) {
                        existingTemplate.setNotSupportControllerVersion(parseVersionsToList((String)dataMap.get("notSupportControllerVersion")));
                    }
                    if (dataMap.containsKey("type")) {
                        existingTemplate.setType((String)dataMap.get("type"));
                    }
                    if (dataMap.containsKey("modelType")) {
                        existingTemplate.setModelType((String)dataMap.get("modelType"));
                    }
                    if (dataMap.containsKey("ippt")) {
                        existingTemplate.setIppt((Boolean)dataMap.get("ippt"));
                    }
                    if (dataMap.containsKey("adoptResp")) {
                        String adoptRespStr = (String) dataMap.get("adoptResp");
                        Map<String, Object> adoptRespMap = null;
                        if (adoptRespStr != null && !adoptRespStr.trim().isEmpty()) {
                            try {
                                adoptRespMap = objectMapper.readValue(adoptRespStr, Map.class);
                            } catch (IOException e) {
                                throw new IllegalArgumentException("解析 adoptResp JSON 失败", e);
                            }
                        }
                        existingTemplate.setAdoptResp(adoptRespMap);
                    }
                    deviceModelTemplateRepository.save(existingTemplate);
                }
            }

            // 更新镜像信息
            if (existingDevice.getImageInfoId() != null) {
                ImageInfoPO existingImageInfo = imageInfoRepository
                        .findById(existingDevice.getImageInfoId())
                        .orElse(null);
                if (existingImageInfo != null) {
                    if (dataMap.containsKey("type")) {
                        existingImageInfo.setType((String)dataMap.get("type"));
                    }
                    if (dataMap.containsKey("controllerVersion")) {
                        existingImageInfo.setControllerVersion((String)dataMap.get("controllerVersion"));
                    }
                    if (dataMap.containsKey("minControllerVersion")) {
                        existingImageInfo.setMinControllerVersion((String)dataMap.get("minControllerVersion"));
                    }
                    if (dataMap.containsKey("notSupportControllerVersion")) {
                        existingImageInfo.setNotSupportControllerVersion(parseVersionsToList((String)dataMap.get("notSupportControllerVersion")));
                    }
                    if (dataMap.containsKey("imageName")) {
                        existingImageInfo.setImageName((String)dataMap.get("imageName"));
                    }

                    // 更新 imgBucketPathMap
                    Map<String, String> imgBucketPathMap = existingImageInfo.getImgBucketPathMap();
                    if (imgBucketPathMap == null) {
                        imgBucketPathMap = new HashMap<>();
                    }
                    if (dataMap.containsKey("smallImgBucketPathForWeb")) {
                        imgBucketPathMap.put("small_img_bucket_path_for_web", (String)dataMap.get("smallImgBucketPathForWeb"));
                    }
                    if (dataMap.containsKey("heatmapImgBucketPathForWeb")) {
                        imgBucketPathMap.put("heatmap_img_bucket_path_for_web", (String)dataMap.get("heatmapImgBucketPathForWeb"));
                    }
                    if (dataMap.containsKey("bigImgBucketPathForWeb")) {
                        imgBucketPathMap.put("big_img_bucket_path_for_web", (String)dataMap.get("bigImgBucketPathForWeb"));
                    }
                    if (dataMap.containsKey("hdpiImgBucketPathForApp")) {
                        imgBucketPathMap.put("hdpi_img_bucket_path_for_app", (String)dataMap.get("hdpiImgBucketPathForApp"));
                    }
                    existingImageInfo.setImgBucketPathMap(imgBucketPathMap);

                    imageInfoRepository.save(existingImageInfo);
                }
            }

            Integer id = existingDevice.getId();
            Integer status = existingDevice.getStatus();

            // 手动设置更新时间
            existingDevice.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            existingDevice.setId(id);
            existingDevice.setStatus(status);

            deviceInfoRepository.save(existingDevice);

        } catch (Exception e) {
            throw new RuntimeException("更新设备数据失败", e);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDataStatus(Integer id, Integer status) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备不存在"));

        deviceInfo.setStatus(status);
        deviceInfoRepository.save(deviceInfo);
    }

    @Override
    public DeviceInfo parseDataDetails(String dataDetails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(dataDetails, DeviceInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("解析网关数据失败", e);
        }
    }

    @Override
    @Transactional
    public DeviceInfoDto findDetailById(Integer id) {
        DeviceInfo deviceInfo = deviceInfoRepository.findById(id).orElseGet(DeviceInfo::new);
        ValidationUtil.isNull(deviceInfo.getId(), "DeviceInfo", "id", id);

        DeviceModelTemplatePO template = null;
        ImageInfoPO imageInfo = null;

        // 获取关联的模板信息
        if (deviceInfo.getModelTemplateId() != null) {
            template = deviceModelTemplateRepository.findById(deviceInfo.getModelTemplateId()).orElse(null);
        }

        // 获取关联的镜像信息
        if (deviceInfo.getImageInfoId() != null) {
            imageInfo = imageInfoRepository.findById(deviceInfo.getImageInfoId()).orElse(null);
        }

        return deviceInfoMapper.toDtoWithImagePaths(deviceInfo, template, imageInfo);
    }



    @Override
    public List<DeviceInfoDto> getAllActiveDeviceDetails() {
        List<DeviceInfo> deviceInfos = deviceInfoRepository.findByStatus(1);
        return deviceInfos.stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceInfoDto> searchActiveDeviceDetails(String keyword) {
        List<DeviceInfo> deviceInfos = deviceInfoRepository.findActiveDevicesByKeyword(keyword);
        return deviceInfos.stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());
    }

    private DeviceInfoDto convertToDetailDto(DeviceInfo deviceInfo) {
        DeviceModelTemplatePO template = null;
        ImageInfoPO imageInfo = null;

        // 获取关联的模板信息
        if (deviceInfo.getModelTemplateId() != null) {
            template = deviceModelTemplateRepository.findById(deviceInfo.getModelTemplateId()).orElse(null);
        }

        // 获取关联的镜像信息
        if (deviceInfo.getImageInfoId() != null) {
            imageInfo = imageInfoRepository.findById(deviceInfo.getImageInfoId()).orElse(null);
        }

        return deviceInfoMapper.toDtoWithImagePaths(deviceInfo, template, imageInfo);
    }


    @Override
    public PageResult<DeviceInfoDto> queryAllWithDetails(DeviceInfoQueryCriteria criteria, Pageable pageable) {
        Page<DeviceInfo> page = deviceInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);

        // 将每条记录转换为包含详细信息的 DTO
        Page<DeviceInfoDto> detailPage = page.map(deviceInfo -> {

            return convertToDetailDto(deviceInfo);
        });

        return PageUtil.toPage(detailPage);
    }

    @Override
    public List<DeviceInfoDto> queryAllWithDetails(DeviceInfoQueryCriteria criteria) {
        List<DeviceInfo> deviceInfos = deviceInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        // 将每条记录转换为包含详细信息的 DTO
        return deviceInfos.stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());
    }

    // 在 DeviceInfoServiceImpl.java 中添加以下方法
    /**
     * 执行设备信息同步操作
     * @param deviceId 设备ID
     * @param status 目标状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncDeviceInfo(Integer deviceId, Integer status) {
        // 获取设备信息
        DeviceInfo deviceInfo = deviceInfoRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("设备信息不存在"));

        // 合法性校验
        if (status.equals(DeviceInfo.STATUS_ONLINE) &&
                !deviceInfo.getStatus().equals(DeviceInfo.STATUS_OFFLINE)) {
            throw new RuntimeException("设备必须处于下线状态才能上线");
        }

        try {
            // 获取关联的模板信息
            DeviceModelTemplatePO template = null;
            if (deviceInfo.getModelTemplateId() != null) {
                template = deviceModelTemplateRepository.findById(deviceInfo.getModelTemplateId())
                        .orElseThrow(() -> new RuntimeException("设备模板信息不存在"));
            }

            // 获取关联的镜像信息
            ImageInfoPO imageInfo = null;
            if (deviceInfo.getImageInfoId() != null) {
                imageInfo = imageInfoRepository.findById(deviceInfo.getImageInfoId())
                        .orElseThrow(() -> new RuntimeException("镜像信息不存在"));
            }

            // 调用模板同步接口
            if (template != null) {
                syncDeviceModelTemplate(template, status);
            }

            // 调用镜像同步接口
            if (imageInfo != null) {
                syncImageInfo(imageInfo, status);
            }

            // 更新设备状态
            deviceInfo.setStatus(status);
            deviceInfoRepository.save(deviceInfo);

        } catch (Exception e) {
            throw new RuntimeException("设备信息同步失败: " + e.getMessage(), e);
        }
    }

    /**
     * 同步设备模板信息到远程服务
     * @param template 设备模板
     * @param status 状态
     */
    private void syncDeviceModelTemplate(DeviceModelTemplatePO template, Integer status) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Step 1: PO -> VO 转换
            DeviceModelTemplateActuateVO vo = DeviceModelTemplateActuateVO.builder()
                    .model(template.getModel())
                    .hwVersion(template.getHwVersion())
                    .version(template.getVersion())
                    .type(template.getType())
                    .controllerVersion(template.getControllerVersion())
                    .minControllerVersion(template.getMinControllerVersion())
                    .notSupportControllerVersion(template.getNotSupportControllerVersion()) // 已经是 List<String>
                    .ippt(template.getIppt())
                    .modelType(template.getModelType())
                    .modelVersion(template.getModelVersion())
                    .adoptResp(objectMapper.writeValueAsString(template.getAdoptResp())) // Map -> JSON String
                    .status(status)
                    .build();

            // Step 2: 构造内层对象 {"deviceModelTemplates": [vo]}
            Map<String, Object> innerBody = new HashMap<>();
            innerBody.put("deviceModelTemplates", List.of(vo));

            // Step 3: 序列化 innerBody 成字符串（保留转义）
            String innerJsonString = objectMapper.writeValueAsString(innerBody);

            // Step 4: 外层包装：{"deviceModelTemplate": "..."}
            Map<String, String> outerBody = new HashMap<>();
            outerBody.put("deviceModelTemplate", innerJsonString);

            // 最终 JSON 字符串（用于传输）
            String finalJson = objectMapper.writeValueAsString(outerBody);

            // Debug 输出
            System.out.println("Final Request Body: " + finalJson);

            // 发送请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(finalJson, headers);
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:9090/actuator/deviceModelTemplate";
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("设备模板同步失败，HTTP状态码: " + response.getStatusCode() + ", 响应: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("设备模板同步异常: " + e.getMessage(), e);
        }
    }

    /**
     * 同步镜像信息到远程服务
     * @param imageInfo 镜像信息
     * @param status 状态
     */
    private void syncImageInfo(ImageInfoPO imageInfo, Integer status) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Step 1: 从 imgBucketPathMap 提取所需字段（安全取值，避免 NPE）
            Map<String, String> pathMap = imageInfo.getImgBucketPathMap() != null ? imageInfo.getImgBucketPathMap() : new HashMap<>();

            String smallImg = getValueByKey(pathMap, "small_img_bucket_path_for_web", "smallImg");
            String bigImg = getValueByKey(pathMap, "big_img_bucket_path_for_web", "bigImg");
            String heatmapImg = getValueByKey(pathMap, "heatmap_img_bucket_path_for_web", "heatmapImg");
            String appImg = getValueByKey(pathMap, "hdpi_img_bucket_path_for_app", "hdpiImg");

            // 构建 ImageBucketPathActuateVO
            ImageBucketPathActuateVO bucketPathVO = ImageBucketPathActuateVO.builder()
                    .smallImgBucketPathForWeb(smallImg)
                    .bigImgBucketPathForWeb(bigImg)
                    .heatmapImgBucketPathForWeb(heatmapImg)
                    .hdpiImgBucketPathForApp(appImg)
                    .build();

            // Step 2: 构建主 VO
            ImageInfoActuateVO vo = ImageInfoActuateVO.builder()
                    .model(imageInfo.getModel())
                    .modelVersion(imageInfo.getModelVersion())
                    .compoundModel(imageInfo.getCompoundModel())
                    .type(imageInfo.getType())
                    .imgName(imageInfo.getImageName()) // 注意 PO 是 imageName，VO 是 imgName
                    .controllerVersion(imageInfo.getControllerVersion())
                    .minControllerVersion(imageInfo.getMinControllerVersion())
                    .notSupportControllerVersion(defaultIfNull(imageInfo.getNotSupportControllerVersion(), ArrayList::new))
                    .imageBucketPathActuateVO(bucketPathVO)
                    .status(status)
                    .build();

            // Step 3: 转成 Map 并添加 status 字段
            Map<String, Object> voMap = objectMapper.convertValue(vo, Map.class);

            // Step 4: 内层包装 {"imageInfos": [...] }
            Map<String, Object> innerBody = new HashMap<>();
            innerBody.put("imageInfos", List.of(voMap));

            String innerJsonString = objectMapper.writeValueAsString(innerBody);

            // Step 5: 外层包装 {"imageInfo": "..."}
            Map<String, String> outerBody = new HashMap<>();
            outerBody.put("imageInfo", innerJsonString);

            String finalJson = objectMapper.writeValueAsString(outerBody);

            // Debug 输出
            System.out.println("Final Request Body: " + finalJson);

            // 发送请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(finalJson, headers);
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:9090/actuator/imageInfo";

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("镜像信息同步失败，HTTP状态码: " + response.getStatusCode() + ", 响应: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("镜像信息同步异常: " + e.getMessage(), e);
        }
    }

    // 工具方法：优先按 JSON key 取，其次尝试驼峰 key
    private String getValueByKey(Map<String, String> map, String snakeKey, String camelSuffix) {
        if (map == null || map.isEmpty()) return null;
        // 先尝试下划线 key
        if (map.containsKey(snakeKey)) {
            return map.get(snakeKey);
        }
        // 再尝试驼峰 key（如 smallImg）
        String camelKey = Character.toLowerCase(camelSuffix.charAt(0)) + camelSuffix.substring(1);
        return map.getOrDefault(camelKey, null);
    }

    // 空值保护
    private <T> T defaultIfNull(T value, Supplier<T> supplier) {
        return value != null ? value : supplier.get();
    }




}

