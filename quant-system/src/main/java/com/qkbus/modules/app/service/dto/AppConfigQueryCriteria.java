package com.qkbus.modules.app.service.dto;

import com.qkbus.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 少林一枝花
 * @date 2021-06-10
 */
@Data
public class AppConfigQueryCriteria {
    @Query
    @ApiModelProperty(value = "配置名")
    private String configName;
}
