
package com.qkbus.logging.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.logging.domain.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 少林一枝花
 * @date 2019-5-22
 */

@Mapper
@Repository
public interface LogMapper extends CoreMapper<SysLog> {


}
