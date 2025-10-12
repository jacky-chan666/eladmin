/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service.mapstruct;

import com.tplink.smb.common.data.management.system.base.BaseMapper;
import com.tplink.smb.common.data.management.system.modules.maint.domain.Deploy;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.DeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Mapper(
    componentModel = "spring",
    uses = {AppMapper.class, ServerDeployMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployMapper extends BaseMapper<DeployDto, Deploy> {}
