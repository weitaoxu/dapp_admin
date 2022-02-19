package com.qkbus.modules.app.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.constant.RedisConstant;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.modules.app.domain.AppConfig;
import com.qkbus.modules.app.service.AppConfigService;
import com.qkbus.modules.app.service.dto.AppConfigDto;
import com.qkbus.modules.app.service.dto.AppConfigQueryCriteria;
import com.qkbus.modules.app.service.mapper.AppConfigMapper;
import com.qkbus.utils.FileUtil;
import com.qkbus.utils.RedisUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 少林一枝花
 * @date 2021-06-10
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AppConfigServiceImpl extends BaseServiceImpl<AppConfigMapper, AppConfig> implements AppConfigService {

    private final IGenerator generator;


    private final RedisUtils redisUtils;

    @Override
    public Map<String, Object> queryAll(AppConfigQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<AppConfig> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), AppConfigDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<AppConfig> queryAll(AppConfigQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(AppConfig.class, criteria));
    }


    @Override
    public void download(List<AppConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppConfigDto Config : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("配置名", Config.getConfigName());
            map.put("配置值", Config.getConfigValue());
            map.put("配置描述", Config.getConfigRemark());
            map.put("状态", Config.getStatus());
            map.put("创建时间", Config.getGmtCreate());
            map.put("最新更新时间", Config.getGmtUpdated());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppConfig resources) {
        this.save(resources);
        this.cacheConfig();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppConfig resources) {
        this.updateById(resources);
        this.cacheConfig();
    }

    @Override
    public void deleteAll(Set<Long> ids) {
        this.removeByIds(ids);
        this.cacheConfig();
    }

    private void cacheConfig() {
        // 初始化系统全局配置信息
        List<AppConfig> quantConfigs = baseMapper.quantConfigs();
        for (AppConfig config : quantConfigs) {
            redisUtils.hset(RedisConstant.SYSTEM_CONFIG, config.getConfigName(), config.getConfigValue());
        }
    }


}
