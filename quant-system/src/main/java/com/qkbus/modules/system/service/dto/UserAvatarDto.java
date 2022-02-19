
package com.qkbus.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
public class UserAvatarDto implements Serializable {

    private Long id;

    /**
     * 真实文件名
     */
    private String realName;

    /**
     * 路径
     */
    private String path;

    /**
     * 大小
     */
    private String size;

    /**
     * 创建时间
     */
    private Timestamp gmtCreate;
}
