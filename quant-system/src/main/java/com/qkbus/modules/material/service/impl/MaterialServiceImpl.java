
package com.qkbus.modules.material.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.modules.material.domain.Material;
import com.qkbus.modules.material.service.MaterialService;
import com.qkbus.modules.material.service.dto.MaterialDto;
import com.qkbus.modules.material.service.dto.MaterialQueryCriteria;
import com.qkbus.modules.material.service.mapper.MaterialMapper;
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
 * @date 2021-05-11
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MaterialServiceImpl extends BaseServiceImpl<MaterialMapper, Material> implements MaterialService {

    private final IGenerator generator;

    @Override
    public Map<String, Object> queryAll(MaterialQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Material> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), MaterialDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<Material> queryAll(MaterialQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(Material.class, criteria));
    }


    @Override
    public void download(List<MaterialDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MaterialDto Material : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("类型", Material.getType());
            map.put("分组类型", Material.getGroupType());
            map.put("标题", Material.getTitle());
            map.put("详情", Material.getContent());
            map.put("地址", Material.getUrl());
            map.put("排序", Material.getSort());
            map.put("点赞", Material.getLikeNum());
            map.put("查看", Material.getLookNum());
            map.put("创建时间", Material.getGmtCreate());
            map.put("修改时间", Material.getGmtUpdated());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Material resources) {
        this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Material resources) {
        this.updateById(resources);
    }

    @Override
    public void deleteAll(Set<Integer> ids) {
        this.removeByIds(ids);
    }
}
