
package com.qkbus.modules.system.service.dto;

import com.qkbus.annotation.Query;
import lombok.Data;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
public class DictDetailQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String label;

    private String dictName;
}
