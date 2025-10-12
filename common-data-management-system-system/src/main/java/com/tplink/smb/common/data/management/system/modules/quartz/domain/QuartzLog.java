/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.quartz.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Entity
@Data
@Table(name = "sys_quartz_log")
public class QuartzLog implements Serializable {

  @Id
  @Column(name = "log_id")
  @ApiModelProperty(value = "ID", hidden = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ApiModelProperty(value = "任务名称", hidden = true)
  private String jobName;

  @ApiModelProperty(value = "bean名称", hidden = true)
  private String beanName;

  @ApiModelProperty(value = "方法名称", hidden = true)
  private String methodName;

  @ApiModelProperty(value = "参数", hidden = true)
  private String params;

  @ApiModelProperty(value = "cron表达式", hidden = true)
  private String cronExpression;

  @ApiModelProperty(value = "状态", hidden = true)
  private Boolean isSuccess;

  @ApiModelProperty(value = "异常详情", hidden = true)
  private String exceptionDetail;

  @ApiModelProperty(value = "执行耗时", hidden = true)
  private Long time;

  @CreationTimestamp
  @ApiModelProperty(value = "创建时间", hidden = true)
  private Timestamp createTime;
}
