/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service.mapstruct;

import com.tplink.smb.common.data.management.system.base.BaseMapper;
import com.tplink.smb.common.data.management.system.modules.maint.domain.ServerDeploy;
import com.tplink.smb.common.data.management.system.modules.maint.service.dto.ServerDeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Mapper(
    componentModel = "spring",
    uses = {},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerDeployMapper extends BaseMapper<ServerDeployDto, ServerDeploy> {}
