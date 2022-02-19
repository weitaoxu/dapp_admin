
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
public class MenuDto implements Serializable {

    private Long id;

    private Integer type;

    private String permission;

    private String name;

    private Long sort;

    private String path;

    private String component;

    private Long pid;

    private Boolean iFrame;

    private Boolean cache;

    private Boolean hidden;


    /**
     *
     */
    private Boolean treeHidden;

    private String componentName;

    private String icon;

    private List<MenuDto> children;

    private Timestamp gmtCreate;


}
