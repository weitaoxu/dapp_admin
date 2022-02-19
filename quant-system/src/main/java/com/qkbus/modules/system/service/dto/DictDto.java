
package com.qkbus.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
public class DictDto implements Serializable {

    /**
     * 字典ID
     */
    private Long id;

    /**
     * 字典名称
     */
    private String name;

    private List<DictDetailDto> dictDetails;

    /**
     * 描述
     */
    private String remark;

    /**
     * 创建日期
     */
    private Timestamp gmtCreate;
}
