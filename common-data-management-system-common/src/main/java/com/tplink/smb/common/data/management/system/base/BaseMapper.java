/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.base;

import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface BaseMapper<D, E> {

  /**
   * DTO转Entity
   *
   * @param dto /
   * @return /
   */
  E toEntity(D dto);

  /**
   * Entity转DTO
   *
   * @param entity /
   * @return /
   */
  D toDto(E entity);

  /**
   * DTO集合转Entity集合
   *
   * @param dtoList /
   * @return /
   */
  List<E> toEntity(List<D> dtoList);

  /**
   * Entity集合转DTO集合
   *
   * @param entityList /
   * @return /
   */
  List<D> toDto(List<E> entityList);
}
