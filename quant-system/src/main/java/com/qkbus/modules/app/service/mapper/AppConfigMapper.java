package com.qkbus.modules.app.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.modules.app.domain.AppConfig;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 少林一枝花
 * @date 2021-06-10
 */

public interface AppConfigMapper extends CoreMapper<AppConfig> {


    @Select("SELECT id, config_name, config_value, config_remark, is_pull_down,status, is_deleted, gmt_create, gmt_updated FROM quant_config WHERE status = 1")
    List<AppConfig> quantConfigs();

}
