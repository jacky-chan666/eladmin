/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.mapstruct;

import com.tplink.smb.common.data.management.system.base.BaseMapper;
import com.tplink.smb.common.data.management.system.modules.system.domain.Dict;
import com.tplink.smb.common.data.management.system.modules.system.service.dto.DictDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictMapper extends BaseMapper<DictDto, Dict> {}
