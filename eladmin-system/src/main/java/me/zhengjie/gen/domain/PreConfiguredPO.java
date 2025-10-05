/*
 * Copyright (c) 2025, TP-Link Corporation Limited. All rights reserved.
 */

package me.zhengjie.gen.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;


/**
 * @author:shanliang
 * @version: 1.0.0
 * @since:2025/2/27
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PreConfiguredPO {

    @Id
    private String id;


    private String antennaGainMapJson;


    private String virtualDevicesJson;
}
 