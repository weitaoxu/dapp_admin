
package com.qkbus.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("sys_job")
public class Job extends BaseEntity {

    /**
     * 岗位ID
     */
    @TableId
    private Long id;

    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称不能为空")
    private String name;


    /**
     * 岗位状态
     */
    private Boolean enabled;

    @TableField(exist = false)
    private Dept dept;

    /**
     * 岗位排序
     */
    private Long sort;


    /**
     * 部门ID
     */
    private Long deptId;


    public void copy(Job source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
