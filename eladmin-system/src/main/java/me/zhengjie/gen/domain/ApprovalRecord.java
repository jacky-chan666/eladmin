// ApprovalRecord.java
package me.zhengjie.gen.domain;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

@Entity
@Data
@Table(name = "approval_record")
public class ApprovalRecord implements Serializable {

    public static final Integer STATUS_PEDING = 0;        // 草稿
    public static final Integer STATUS_APPROVED = 1;       // 审批通过
    public static final Integer STATUS_REJECTED = 2;       // 审批驳回
    public static final Integer STEP_ONE = 1;       // 第一阶段审批
    public static final Integer STEP_TWO = 2;       // 第二阶段审批

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

    @Column(name = "approval_status")
    private Integer approvalStatus; // 0-待审批，1-通过，2-驳回

    @Column(name = "comment")
    private String comment;

    @Column(name = "approved_at")
    private Timestamp approvedAt;
}
