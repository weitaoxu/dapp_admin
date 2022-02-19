package com.qkbus.modules.app.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.*;
import com.qkbus.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2021-05-13
 */
@Data
@TableName("app_version")
public class AppVersion extends BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "Android或者IOS")
    private String client;

    @ApiModelProperty(value = "Code")
    private Integer code;

    @ApiModelProperty(value = "version")
    private String version;

    @ApiModelProperty(value = "更新包URL")
    private String url;

    @ApiModelProperty(value = "描述")
    @TableField(value = "`describe`")
    private String describe;
    @TableLogic
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;

    public void copy(AppVersion source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
