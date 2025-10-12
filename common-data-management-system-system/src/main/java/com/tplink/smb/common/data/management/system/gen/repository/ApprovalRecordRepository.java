/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.repository;

import com.tplink.smb.common.data.management.system.gen.domain.ApprovalRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRecordRepository
    extends JpaRepository<ApprovalRecord, Integer>, JpaSpecificationExecutor<ApprovalRecord> {

  List<ApprovalRecord> findByApplicationFormIdOrderByRoundAscStepOrderAscApprovedAtAsc(
      Integer applicationFormId);

  List<ApprovalRecord> findByApproverUserNameAndApprovalStatusInOrderByApprovedAtDesc(
      String approverUserName, List<Integer> approvalStatuses);

  @Query("SELECT COALESCE(MAX(round), 0) FROM ApprovalRecord WHERE applicationFormId = ?1")
  Integer findMaxRoundByApplicationFormId(Integer applicationFormId);

  // 修改 ApprovalRecordRepository.java，确保有以下方法
  @Modifying
  void deleteByApplicationFormId(Integer applicationFormId);
}
