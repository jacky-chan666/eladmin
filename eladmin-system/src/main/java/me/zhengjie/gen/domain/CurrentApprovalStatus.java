// CurrentApprovalStatus.java
package me.zhengjie.gen.domain;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "current_approval_status")
public class CurrentApprovalStatus implements Serializable {

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

    @Column(name = "approver_name")
    private String approverName;

    @Column(name = "status")
    private Integer status; // 0-待审批，1-已审批
}
