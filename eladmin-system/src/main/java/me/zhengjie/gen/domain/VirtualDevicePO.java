/*
 * Copyright (c) 2025, TP-LINK Co.,Ltd. All rights reserved.
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
 * Description of this file
 * @author Jiang Guoqing
 * @version 1.0
 * @since 2025/6/28
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class VirtualDevicePO {

    @Id
    private String id;


    private String model;


    private String modelVersion;


    private String countryCode;


    private String showModel;


    private boolean support2g;


    private boolean support5g;


    private boolean support5g1;


    private boolean support5g2;


    private boolean support6g;


    private List<NetworkParamPO> networkParamList;

}
 