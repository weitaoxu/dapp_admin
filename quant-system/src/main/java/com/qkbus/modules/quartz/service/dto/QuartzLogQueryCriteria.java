
package com.qkbus.modules.quartz.service.dto;

import com.qkbus.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Data
public class QuartzLogQueryCriteria {


    @Query(blurry = "baenName,cronExpression,jobName,methodName,params")
    private String blurry;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> gmtCreate;


}
