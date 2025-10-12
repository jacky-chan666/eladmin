/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.repository;

import com.tplink.smb.common.data.management.system.gen.domain.CurrentApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CurrentApprovalStatusRepository
    extends JpaRepository<CurrentApprovalStatus, Integer>,
        JpaSpecificationExecutor<CurrentApprovalStatus> {

  List<CurrentApprovalStatus> findByApproverUserNameAndStatusOrderByApplicationFormId(
      String approverUserName, Integer status);

  List<CurrentApprovalStatus> findByApplicationFormIdAndRound(
      Integer applicationFormId, Integer round);

  @Modifying
  void deleteByApplicationFormIdAndRound(Integer applicationFormId, Integer round);

  @Query(
      "SELECT COUNT(*) FROM CurrentApprovalStatus WHERE applicationFormId = ?1 AND round = ?2 AND stepOrder = ?3 AND status = 0")
  Integer countPendingByApplicationFormIdAndRoundAndStepOrder(
      Integer applicationFormId, Integer round, Integer stepOrder);

  @Modifying
  void deleteByApplicationFormId(Integer applicationFormId);
}
