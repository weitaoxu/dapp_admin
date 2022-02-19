
package com.qkbus.modules.material.service.dto;

import com.qkbus.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 少林一枝花
 * @date 2021-05-11
 */
@Data
public class MaterialGroupQueryCriteria {
    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "名称")
    private String name;
}
