package me.zhengjie.gen.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;




/**
 * @website https://eladmin.vip
 * @description /
 * @author chen jiayuan
 * @date 2025-09-16
 **/
@Entity
@Data
@Table(name="gateway_info")
public class GateWayInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "model",nullable = false, unique = true)
    @NotBlank
    @ApiModelProperty(value = "sb模型类型")
    private String sbmodel;

    @Column(name = "`model_version`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "sb模型版本")
    private String modelVersion;

    @Column(name = "sbname")
    @ApiModelProperty(value = "sb设备名称")
    private String sbname;

    @Column(name = "type")
    @ApiModelProperty(value = "sb设备类型")
    private String sbtype;

    @Column(name = "`manufacturer`")
    @ApiModelProperty(value = "制造商")
    private String manufacturer;

    @Column(name = "`specifications`")
    @ApiModelProperty(value = "规格参数")
    private String specifications;

    @Column(name = "status")
    @ApiModelProperty(value = "状态：0-下线，1-上线")
    private Integer status = 1; // 默认上线

    @Column(name = "`created_at`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @Column(name = "`updated_at`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedAt;

    public void copy(me.zhengjie.gen.domain.GateWayInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
