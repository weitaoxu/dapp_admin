
package com.qkbus.modules.system.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.modules.system.domain.DictDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */

public interface DictDetailMapper extends CoreMapper<DictDetail> {

    @Select("<script>SELECT d.* from sys_dict_detail d LEFT JOIN sys_dict t on d.dict_id = t.id where 1=1 <if test = \"label !=null\" > and d.label LIKE concat('%', #{label}, '%') </if> <if test = \"dictName != ''||dictName !=null\" > AND t.name = #{dictName} </if></script>")
    List<DictDetail> selectDictDetailList(@Param("label") String label, @Param("dictName") String dictName);
}
