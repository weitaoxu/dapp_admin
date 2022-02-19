
package com.qkbus.modules.app.service.dto;

import com.qkbus.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 少林一枝花
 * @date 2021-05-13
 */
@Data
public class AppVersionQueryCriteria {
    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "Code")
    private Integer code;
    /**
     * 模糊
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String describe;
    /**
     * BETWEEN
     */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> gmtCreate;
}
