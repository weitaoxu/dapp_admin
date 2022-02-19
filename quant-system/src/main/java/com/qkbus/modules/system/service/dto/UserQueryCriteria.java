
package com.qkbus.modules.system.service.dto;

import com.qkbus.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
public class UserQueryCriteria {

    @Query
    private Long id;

    @Query(propName = "deptId", type = Query.Type.IN)
    private Set<Long> deptIds;

    @Query(blurry = "email,username,nickName")
    private String blurry;

    @Query
    private Boolean enabled;

    private Long deptId;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> gmtCreate;
}
