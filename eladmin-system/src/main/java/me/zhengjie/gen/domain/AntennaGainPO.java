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
import lombok.experimental.Accessors;
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
@Accessors(chain = true)

public class AntennaGainPO {


    private String model;


    private String modelVersion;

    @Id
    private String deviceKey;


    private String apType;


    private String relateDeviceKey;


    private List<BandPO> bandList;

}
 