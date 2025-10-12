/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.tplink.smb.common.data.management.system.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@Entity
@Table(name = "tool_s3_storage")
@EqualsAndHashCode(callSuper = true)
public class S3Storage extends BaseEntity implements Serializable {

  @Id
  @Column(name = "storage_id")
  @ApiModelProperty(value = "ID", hidden = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @ApiModelProperty(value = "文件名称")
  private String fileName;

  @NotBlank
  @ApiModelProperty(value = "真实存储的名称")
  private String fileRealName;

  @NotBlank
  @ApiModelProperty(value = "文件大小")
  private String fileSize;

  @NotBlank
  @ApiModelProperty(value = "文件MIME 类型")
  private String fileMimeType;

  @NotBlank
  @ApiModelProperty(value = "文件类型")
  private String fileType;

  @NotBlank
  @ApiModelProperty(value = "文件路径")
  private String filePath;

  public void copy(S3Storage source) {
    BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
  }
}
