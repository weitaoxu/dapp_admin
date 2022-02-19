
package com.qkbus.tools.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */

@Getter
@Setter
@TableName("tools_local_storage")
public class LocalStorage implements Serializable {

    @TableId
    private Long id;


    /**
     * 文件真实的名称
     */
    private String realName;


    /**
     * 文件名
     */
    private String name;


    /**
     * 后缀
     */
    private String suffix;


    /**
     * 路径
     */
    private String path;


    /**
     * 类型
     */

    private String type;


    /**
     * 大小
     */

    private String size;


    /**
     * 操作人
     */

    private String operate;


    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp gmtCreate;


    public LocalStorage(String realName, String name, String suffix, String path, String type, String size, String operate) {
        this.realName = realName;
        this.name = name;
        this.suffix = suffix;
        this.path = path;
        this.type = type;
        this.size = size;
        this.operate = operate;
    }

    public void copy(LocalStorage source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
