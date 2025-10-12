package com.tplink.smb.common.data.management.system.gen.domain; // ApplicationForm.java

/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @website https://eladmin.vip
 * @description /
 * @author Chen Jiayuan
 * @date 2025-09-18
 */
@Entity
@Data
@Table(name = "application_form")
public class ApplicationForm implements Serializable {

  // 状态常量定义
  public static final Integer STATUS_DRAFT = -1; // 草稿
  public static final Integer STATUS_SUBMITTED = 0; // 已提交（新增状态）
  public static final Integer STATUS_PENDING = 1; // 待审批（原0改为1）
  public static final Integer STATUS_APPROVED = 2; // 审批通过（原1改为2）
  public static final Integer STATUS_COMPLETED = 3;
  public static final Integer STATUS_FIRMWARE_VERIFY = 4; // 固件校验中（原2改为3）
  public static final Integer STATUS_FIRMWARE_FAILED = 5; // 固件校验失败（原3改为4）
  public static final Integer STATUS_GRAY_SYNCING = 6; // 同步中（原4改为5）
  public static final Integer STATUS_GRAY_SYNC_FAILED = 7; // 同步失败（原5改为6）
  public static final Integer STATUS_SYNCING = 8; // 同步中（原4改为5）
  public static final Integer STATUS_SYNC_FAILED = 9; // 同步失败（原5改为6）
  public static final Integer STATUS_REJECTED = 10; // 已驳回（原7改为8）
  public static final Integer STATUS_AUTO_PROCESSING = 11; // 自动处理中（原8改为9）
  public static final Integer STATUS_AUTO_FAILED = 12; // 自动处理失败（原9改为10）
  public static final Integer STATUS_MANUAL_TRIGGERED = 13; // 手动触发（原10改为11）
  public static final Integer STATUS_WITHDRAWN = 14; // 已撤回（新增状态）

  // 申请单类型常量
  public static final int APPLICATION_TYPE_ADD = 1;
  public static final int APPLICATION_TYPE_EDIT = 2;
  public static final int APPLICATION_TYPE_ONLINE = 3;
  public static final int APPLICATION_TYPE_OFFLINE = 4;

  // 申请单数据类型常量
  public static final int APPLICATION_DATATYPE_DEVICEINFO = 1;
  public static final int APPLICATION_DATATYPE_GATEWAYINFO = 2;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ApiModelProperty(value = "id")
  private Integer id;

  @Column(name = "uuid", unique = true, nullable = false)
  @ApiModelProperty(value = "申请单UUID")
  private String uuid;

  @Column(name = "`applicant_username`", nullable = false)
  @NotBlank
  @ApiModelProperty(value = "申请人用户名")
  private String applicantUserName;

  @Column(name = "department")
  @ApiModelProperty(value = "所属部门")
  private String department;

  @Column(name = "`application_data_id`", unique = true)
  @ApiModelProperty(value = "申请数据id")
  private Integer applicationDataId;

  @Column(name = "`application_type`", nullable = false)
  @NotNull
  @ApiModelProperty(value = "申请单类型：新增，修改，上线，下线")
  private Integer applicationType;

  // 修改 ApplicationForm.java 中的 applicationDataType 字段注释
  @Column(name = "`application_data_type`", nullable = false)
  @NotNull
  @ApiModelProperty(value = "申请单数据类型：1-deviceInfo，2-gatewayInfo")
  private Integer applicationDataType;

  @Column(name = "`application_title`", nullable = false)
  @NotBlank
  @ApiModelProperty(value = "申请单标题")
  private String applicationTitle;

  @Column(name = "`application_reason`")
  @ApiModelProperty(value = "申请理由")
  private String applicationReason;

  @Column(name = "round")
  @ApiModelProperty(value = "审批轮次")
  private Integer round = 0;

  @Column(name = "status", nullable = false)
  @NotNull
  @ApiModelProperty(value = "申请状态")
  private Integer status;

  // 删除原有的审批人字段，新增6个审批人字段
  @Column(name = "`approver1`")
  @ApiModelProperty(value = "审批人1")
  private String approver1;

  @Column(name = "`approver1_level`")
  @ApiModelProperty(value = "审批人1层级")
  private Integer approver1Level;

  @Column(name = "`approver2`")
  @ApiModelProperty(value = "审批人2")
  private String approver2;

  @Column(name = "`approver2_level`")
  @ApiModelProperty(value = "审批人2层级")
  private Integer approver2Level;

  @Column(name = "`approver3`")
  @ApiModelProperty(value = "审批人3")
  private String approver3;

  @Column(name = "`approver3_level`")
  @ApiModelProperty(value = "审批人3层级")
  private Integer approver3Level;

  @Column(name = "`approver4`")
  @ApiModelProperty(value = "审批人4")
  private String approver4;

  @Column(name = "`approver4_level`")
  @ApiModelProperty(value = "审批人4层级")
  private Integer approver4Level;

  @Column(name = "`approver5`")
  @ApiModelProperty(value = "审批人5")
  private String approver5;

  @Column(name = "`approver5_level`")
  @ApiModelProperty(value = "审批人5层级")
  private Integer approver5Level;

  @Column(name = "`approver6`")
  @ApiModelProperty(value = "审批人6")
  private String approver6;

  @Column(name = "`approver6_level`")
  @ApiModelProperty(value = "审批人6层级")
  private Integer approver6Level;

  @Column(name = "`approval_records`", columnDefinition = "JSON")
  @ApiModelProperty(value = "审批记录（JSON格式存储）")
  private String approvalRecords;

  @Column(name = "`data_details`")
  @ApiModelProperty(value = "设备信息详情（JSON格式存储）")
  private String dataDetails;

  @Column(name = "`created_at`")
  @ApiModelProperty(value = "createdAt")
  private Timestamp createdAt;

  @Column(name = "`updated_at`")
  @ApiModelProperty(value = "updatedAt")
  private Timestamp updatedAt;

  public void copy(ApplicationForm source) {
    BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
  }
}
