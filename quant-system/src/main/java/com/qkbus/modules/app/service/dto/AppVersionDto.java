
package com.qkbus.modules.app.service.dto;

import com.qkbus.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppVersionDto extends BaseDto implements Serializable {
    private Long id;
    @ApiModelProperty("Android或者IOS")
    private String client;
    @ApiModelProperty("Code")
    private Integer code;
    @ApiModelProperty("version")
    private String version;
    @ApiModelProperty("更新包URL")
    private String url;
    @ApiModelProperty("描述")
    private String describe;

}
