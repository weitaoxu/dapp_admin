package com.qkbus.modules.app.service.dto;

import com.qkbus.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2021-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppConfigDto extends BaseDto implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("配置名")
    private String configName;

    @ApiModelProperty("配置值")
    private String configValue;

    @ApiModelProperty("配置描述")
    private String configRemark;

    @ApiModelProperty("状态")
    private Integer status;

}
