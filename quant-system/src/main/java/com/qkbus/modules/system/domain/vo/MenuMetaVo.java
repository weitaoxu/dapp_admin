
package com.qkbus.modules.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2021-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class MenuMetaVo implements Serializable {

    private String title;

    private String icon;

    private Boolean noCache;
}
