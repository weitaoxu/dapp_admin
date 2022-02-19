
package com.qkbus.tools.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */

@Data
@TableName("tools_qiniu_content")
public class QiniuContent implements Serializable {

    /**
     * ID
     */
    @TableId
    private Long id;


    /**
     * Bucket 识别符
     */
    private String bucket;


    /**
     * 文件名称
     */
    @TableField("name")
    private String name;


    /**
     * 文件大小
     */
    private String size;


    /**
     * 文件类型：私有或公开
     */
    private String type;


    /**
     * 上传或同步的时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp gmtUpdated;


    /**
     * 文件url
     */
    private String url;


    private String suffix;


    public void copy(QiniuContent source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
