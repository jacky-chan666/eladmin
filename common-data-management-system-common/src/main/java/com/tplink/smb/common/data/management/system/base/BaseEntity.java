/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

  @CreatedBy
  @Column(name = "create_by", updatable = false)
  @ApiModelProperty(value = "创建人", hidden = true)
  private String createBy;

  @LastModifiedBy
  @Column(name = "update_by")
  @ApiModelProperty(value = "更新人", hidden = true)
  private String updateBy;

  @CreationTimestamp
  @Column(name = "create_time", updatable = false)
  @ApiModelProperty(value = "创建时间: yyyy-MM-dd HH:mm:ss", hidden = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp createTime;

  @UpdateTimestamp
  @Column(name = "update_time")
  @ApiModelProperty(value = "更新时间: yyyy-MM-dd HH:mm:ss", hidden = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp updateTime;

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    Field[] fields = this.getClass().getDeclaredFields();
    try {
      for (Field f : fields) {
        f.setAccessible(true);
        builder.append(f.getName(), f.get(this)).append("\n");
      }
    } catch (Exception e) {
      builder.append("toString builder encounter an error");
    }
    return builder.toString();
  }

  /* 分组校验 */
  public @interface Create {}

  /* 分组校验 */
  public @interface Update {}
}
