
package com.qkbus.modules.system.service.dto;

import com.qkbus.annotation.Query;
import lombok.Data;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
public class RoleQueryCriteria {


    @Query(type = Query.Type.GREATER_THAN)
    private Integer level;


    private Integer page;
    private Integer size;

}
