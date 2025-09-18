// PendingApprovalDto.java
package me.zhengjie.gen.service.dto;

import lombok.Data;
import me.zhengjie.gen.service.dto.DeviceApplicationFormDto;
import io.swagger.annotations.ApiModelProperty;

@Data
public class PendingApprovalDto {

    @ApiModelProperty(value = "申请单信息")
    private DeviceApplicationFormDto applicationForm;

    @ApiModelProperty(value = "审批轮次")
    private Integer round;

    @ApiModelProperty(value = "步骤顺序")
    private Integer stepOrder;
}
