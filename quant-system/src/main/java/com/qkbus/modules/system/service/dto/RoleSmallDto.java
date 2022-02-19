
package com.qkbus.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2021-11-23
 */
@Data
public class RoleSmallDto implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}
