/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Builder
@Setter
@Getter
@Accessors(chain = true)
public class BandPO {

  private String bandFrequency;

  private String maxNoAntennaGainMeters;

  private List<String> antennaGain;
}
