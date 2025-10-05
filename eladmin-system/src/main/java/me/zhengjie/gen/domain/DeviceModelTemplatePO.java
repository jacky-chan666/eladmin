/*
 * Copyright (c) 2024, TP-Link Corporation Limited. All rights reserved.
 */

package me.zhengjie.gen.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

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

public class DeviceModelTemplatePO {
    @Id
    private String id;


    private String model;


    private String hwVersion;


    private String version;


    private String controllerVersion;


    private String minControllerVersion;


    private List<String> notSupportControllerVersion;


    private String type;


    private String modelType;


    private Boolean ippt;


    private String modelVersion;


    private String adoptResp;
}
 