// CurrentApprovalStatusRepository.java
package me.zhengjie.gen.repository;

import me.zhengjie.gen.domain.CurrentApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CurrentApprovalStatusRepository extends JpaRepository<CurrentApprovalStatus, Integer>, JpaSpecificationExecutor<CurrentApprovalStatus> {

    List<CurrentApprovalStatus> findByApproverNameAndStatusOrderByApplicationFormId(String approverName, Integer status);

    List<CurrentApprovalStatus> findByApplicationFormIdAndRound(Integer applicationFormId, Integer round);

    @Modifying
    void deleteByApplicationFormIdAndRound(Integer applicationFormId, Integer round);

    @Query("SELECT COUNT(*) FROM CurrentApprovalStatus WHERE applicationFormId = ?1 AND round = ?2 AND stepOrder = ?3 AND status = 0")
    Integer countPendingByApplicationFormIdAndRoundAndStepOrder(Integer applicationFormId, Integer round, Integer stepOrder);
}
