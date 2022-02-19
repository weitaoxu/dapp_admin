
package com.qkbus.tools.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Data
public class QiniuConfigDto implements Serializable {

    /**
     * ID
     */
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
}
