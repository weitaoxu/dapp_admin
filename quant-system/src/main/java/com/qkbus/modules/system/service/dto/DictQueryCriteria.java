
package com.qkbus.modules.system.service.dto;

import com.qkbus.annotation.Query;
import lombok.Data;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
public class DictQueryCriteria {

    @Query(blurry = "name,remark")
    private String blurry;
}
