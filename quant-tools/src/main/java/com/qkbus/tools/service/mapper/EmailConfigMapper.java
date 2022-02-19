
package com.qkbus.tools.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.tools.domain.EmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Mapper
@Repository
public interface EmailConfigMapper extends CoreMapper<EmailConfig> {

}
