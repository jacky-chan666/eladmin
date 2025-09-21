// PendingApprovalDto.java
package me.zhengjie.gen.service.dto;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

@Data
public class PendingApprovalDto {

    @ApiModelProperty(value = "申请单信息")
    private ApplicationFormDto applicationForm;

    @ApiModelProperty(value = "审批轮次")
    private Integer round;

    @ApiModelProperty(value = "步骤顺序")
    private Integer stepOrder;
}
