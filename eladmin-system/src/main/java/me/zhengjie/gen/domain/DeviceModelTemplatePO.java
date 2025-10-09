/*
 * Copyright (c) 2024, TP-Link Corporation Limited. All rights reserved.
 */

package me.zhengjie.gen.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zhengjie.gen.utils.GenericJsonConverter;
import me.zhengjie.gen.utils.StringListConverter;
import me.zhengjie.gen.utils.StringMapConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;


/**
 * @author Zhou Zhuoran
 * @version 1.0
 * @since 2024/12/9
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_model_template")
public class DeviceModelTemplatePO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "model")
    private String model;

    @Column(name = "hw_version")
    private String hwVersion;

    @Column(name = "version")
    private String version;

    @Column(name = "controller_version")
    private String controllerVersion;

    @Column(name = "min_controller_version")
    private String minControllerVersion;

    @Column(name = "not_support_controller_version", columnDefinition = "JSON")
    @Convert(converter = StringListConverter.class)
    private List<String> notSupportControllerVersion;

    @Column(name = "type")
    private String type;

    @Column(name = "model_type")
    private String modelType;

    @Column(name = "ippt")
    private Boolean ippt;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "adopt_resp", columnDefinition = "JSON")
    @Convert(converter = GenericJsonConverter.class)
    private Map<String, Object> adoptResp; // 支持嵌套结构！

}
