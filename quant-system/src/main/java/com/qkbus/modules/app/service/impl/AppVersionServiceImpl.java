
package com.qkbus.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.modules.app.domain.AppVersion;
import com.qkbus.modules.app.service.AppVersionService;
import com.qkbus.modules.app.service.dto.AppVersionDto;
import com.qkbus.modules.app.service.dto.AppVersionQueryCriteria;
import com.qkbus.modules.app.service.mapper.AppVersionMapper;
import com.qkbus.utils.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author 少林一枝花
 * @date 2021-05-13
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AppVersionServiceImpl extends BaseServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    private final IGenerator generator;

    @Override
    public Map<String, Object> queryAll(AppVersionQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<AppVersion> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), AppVersionDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<AppVersion> queryAll(AppVersionQueryCriteria criteria) {
        QueryWrapper predicate = QueryHelpPlus.getPredicate(AppVersion.class, criteria);
        predicate.select("id, code, client, gmt_updated, `describe`, gmt_create, version, url");
        return baseMapper.selectList(predicate);
    }


    @Override
    public void download(List<AppVersionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppVersionDto AppVersion : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Android或者IOS", AppVersion.getClient());
            map.put("Code", AppVersion.getCode());
            map.put("version", AppVersion.getVersion());
            map.put("更新包URL", AppVersion.getUrl());
            map.put("描述", AppVersion.getDescribe());
            map.put("创建时间", AppVersion.getGmtCreate());
            map.put("修改时间", AppVersion.getGmtUpdated());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppVersion resources) {
        this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppVersion resources) {
        this.updateById(resources);
    }

    @Override
    public void deleteAll(Set<Long> ids) {
        this.removeByIds(ids);
    }
}
