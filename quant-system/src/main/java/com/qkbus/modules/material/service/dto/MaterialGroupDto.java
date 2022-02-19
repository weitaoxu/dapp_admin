
package com.qkbus.modules.material.service.dto;

import com.qkbus.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2021-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MaterialGroupDto extends BaseDto implements Serializable {
    @ApiModelProperty("自增ID")
    private Integer id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("排序")
    private Integer sort;

}
