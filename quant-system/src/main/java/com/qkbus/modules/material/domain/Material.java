package com.qkbus.modules.material.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qkbus.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2021-05-11
 */
@Data
@TableName("material")
public class Material extends BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "自增ID")
    private Integer id;

    @ApiModelProperty(value = "类型   1是图片  2是视频 3是  轮播视频")
    private Integer type;

    @ApiModelProperty(value = "分组类型")
    private Integer groupType;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "详情")
    private String content;

    @ApiModelProperty(value = "地址")
    private String url;

    @ApiModelProperty(value = "排序")
    private Integer sort;


    @ApiModelProperty(value = "点赞")
    private Integer likeNum;

    @ApiModelProperty(value = "查看")
    private Integer lookNum;


    @TableLogic
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;


    public void copy(Material source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
