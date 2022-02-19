
package com.qkbus.modules.material.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.qkbus.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2021-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeDto extends BaseDto implements Serializable {
    @ApiModelProperty("主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("类型")
    private Integer noticeType;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("状态")
    private Integer status;

}
