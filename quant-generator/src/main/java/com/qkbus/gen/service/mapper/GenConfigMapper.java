
package com.qkbus.gen.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.gen.domain.GenConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GenConfigMapper extends CoreMapper<GenConfig> {
}
