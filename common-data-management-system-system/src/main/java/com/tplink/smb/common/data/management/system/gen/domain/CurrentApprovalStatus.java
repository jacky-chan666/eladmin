/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.domain;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "current_approval_status")
public class CurrentApprovalStatus implements Serializable {
  public static final Integer STATUS_PEDING = 0; // 草稿
  public static final Integer STATUS_COMPLETED = 1; // 审批完成
  public static final Integer STEP_ONE = 1; // 第一阶段审批
  public static final Integer STEP_TWO = 2; // 第二阶段审批

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "application_form_id")
  private Integer applicationFormId;

  @Column(name = "round")
  private Integer round;

  @Column(name = "step_order")
  private Integer stepOrder;

  @Column(name = "approver_role")
  private String approverRole;

  @Column(name = "approver_username")
  private String approverUserName;

  @Column(name = "status")
  private Integer status; // 0-待审批，1-已审批
}
