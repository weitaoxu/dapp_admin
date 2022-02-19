
package com.qkbus.tools.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */

@Data
@TableName("tools_qiniu_config")
public class QiniuConfig implements Serializable {

    /**
     * ID
     */
    @TableId
    private Long id;


    /**
     * accessKey
     */
    private String accessKey;


    /**
     * Bucket 识别符
     */
    private String bucket;


    /**
     * 外链域名
     */
    private String host;


    /**
     * secretKey
     */
    private String secretKey;


    /**
     * 空间类型
     */
    private String type;


    /**
     * 机房
     */
    private String zone;


    public void copy(QiniuConfig source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
