/*
 * Copyright (c) 2024, TP-Link Corporation Limited. All rights reserved.
 */

package me.zhengjie.gen.domain;

import java.util.List;
import java.util.Map;

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

public class ImageInfoPO {
    @Id
    private String compoundModel;


    private String model;


    private String modelVersion;


    private String type;


    private String controllerVersion;


    private String minControllerVersion;

    private List<String> notSupportControllerVersion;


    private String imageName;


    private Map<String, String> imgBucketPathMap;

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
 