
package com.qkbus.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qkbus.common.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
@TableName("sys_dict")
public class Dict extends BaseEntity {

    /**
     * 字典ID
     */
    @TableId
    private Long id;


    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    private String name;


    /**
     * 描述
     */
    private String remark;


    public void copy(Dict source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
