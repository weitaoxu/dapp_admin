
package com.qkbus.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("sys_dept")
public class Dept extends BaseEntity {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String name;


    /**
     * 上级部门
     */
    private Long pid;


    /**
     * 状态
     */
    private Boolean enabled;


    public void copy(Dept source) {

        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
