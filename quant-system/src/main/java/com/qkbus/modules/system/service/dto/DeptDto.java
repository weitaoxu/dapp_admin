
package com.qkbus.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
public class DeptDto implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 上级部门
     */
    private Long pid;

    /**
     * 状态
     */
    private Boolean enabled;

    private List<DeptDto> children;

    /**
     * 创建日期
     */
    private Timestamp gmtCreate;

    public String getLabel() {
        return name;
    }
}
