
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
public class MaterialDto extends BaseDto implements Serializable {
    @ApiModelProperty("自增ID")
    private Integer id;
    @ApiModelProperty("类型   1是图片  2是视频 3是  轮播视频")
    private Integer type;
    @ApiModelProperty("分组类型")
    private Integer groupType;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("详情")
    private String content;
    @ApiModelProperty("地址")
    private String url;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("点赞")
    private Integer likeNum;
    @ApiModelProperty("查看")
    private Integer lookNum;
}
