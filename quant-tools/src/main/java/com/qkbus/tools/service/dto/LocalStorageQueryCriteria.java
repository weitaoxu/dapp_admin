
package com.qkbus.tools.service.dto;

import com.qkbus.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Data
public class LocalStorageQueryCriteria {

    @Query(blurry = "name,suffix,type,operate,size")
    private String blurry;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> gmtCreate;
}
