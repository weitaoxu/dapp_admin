
package com.qkbus.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author 少林一枝花
 * @date 2021-12-03
 */
@Data
public class PermissionDto implements Serializable {

    private Long id;

    private String name;

    private Long pid;

    private String alias;

    private Timestamp gmtCreate;

    private List<PermissionDto> children;

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                ", alias='" + alias + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }
}
