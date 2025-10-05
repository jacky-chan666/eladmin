/*
 * Copyright (c) 2025, TP-LINK Co.,Ltd. All rights reserved.
 */
package me.zhengjie.gen.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Description of this file
 * @author Jiang Guoqing
 * @version 1.0
 * @since 2025/6/28
 */
@Builder
@Setter
@Getter
@Accessors(chain = true)
public class NetworkParamPO {


    private String transmitterPower;


    private String transmitterPowerMin;


    private String transmitterPowerMax;


    private List<String> channelSupport;

}
 