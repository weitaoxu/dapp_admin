
package com.qkbus.tools.service.dto;

import com.qkbus.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 少林一枝花
 * @date 2019-6-4 09:54:37
 */
@Data
public class QiniuQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> gmtCreate;
}
