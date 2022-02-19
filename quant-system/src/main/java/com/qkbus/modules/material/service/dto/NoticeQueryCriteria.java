
package com.qkbus.modules.material.service.dto;

import com.qkbus.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 少林一枝花
 * @date 2021-05-14
 */
@Data
public class NoticeQueryCriteria {
    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "类型")
    private Integer noticeType;
}
