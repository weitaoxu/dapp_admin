
package com.qkbus.modules.material.service.dto;

import com.qkbus.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 少林一枝花
 * @date 2021-05-11
 */
@Data
public class MaterialQueryCriteria {
    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "类型   1是图片  2是视频 3是  轮播视频")
    private Integer type;
    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "分组类型")
    private Integer groupType;
    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "标题")
    private String title;
}
