
package com.qkbus.gen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.gen.domain.GenConfig;
import com.qkbus.gen.service.GenConfigService;
import com.qkbus.gen.service.mapper.GenConfigMapper;
import com.qkbus.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author 少林一枝花
 * @date 2019-01-14
 */
@Service
public class GenConfigServiceImpl extends BaseServiceImpl<GenConfigMapper, GenConfig> implements GenConfigService {

    @Override
    public GenConfig find(String tableName) {
        GenConfig genConfig = this.getOne(new LambdaQueryWrapper<GenConfig>().eq(GenConfig::getTableName, tableName));
        if (genConfig == null) {
            return new GenConfig(tableName);
        }
        return genConfig;
    }

    @Override
    public GenConfig update(String tableName, GenConfig genConfig) {
        // 如果 api 路径为空，则自动生成路径
        if (StringUtils.isBlank(genConfig.getApiPath())) {
            String separator = File.separator;
            String[] paths;
            String symbol = "\\";
            if (symbol.equals(separator)) {
                paths = genConfig.getPath().split("\\\\");
            } else {
                paths = genConfig.getPath().split(File.separator);
            }
            StringBuilder api = new StringBuilder();
            for (String path : paths) {
                api.append(path);
                api.append(separator);
                if ("src".equals(path)) {
                    api.append("api");
                    break;
                }
            }
            genConfig.setApiPath(api.toString());
        }
        this.saveOrUpdate(genConfig);
        return genConfig;
    }
}
