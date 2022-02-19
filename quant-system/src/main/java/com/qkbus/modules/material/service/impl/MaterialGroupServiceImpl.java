
package com.qkbus.modules.material.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.modules.material.domain.MaterialGroup;
import com.qkbus.modules.material.service.MaterialGroupService;
import com.qkbus.modules.material.service.MaterialService;
import com.qkbus.modules.material.service.dto.MaterialGroupDto;
import com.qkbus.modules.material.service.dto.MaterialGroupQueryCriteria;
import com.qkbus.modules.material.service.mapper.MaterialGroupMapper;
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
public class MaterialGroupServiceImpl extends BaseServiceImpl<MaterialGroupMapper, MaterialGroup> implements MaterialGroupService {

    private final IGenerator generator;

    private final MaterialService materialService;

    @Override

    public Map<String, Object> queryAll(MaterialGroupQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<MaterialGroup> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), MaterialGroupDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<MaterialGroup> queryAll(MaterialGroupQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(MaterialGroup.class, criteria));
    }


    @Override
    public void download(List<MaterialGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MaterialGroupDto MaterialGroup : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", MaterialGroup.getName());
            map.put("排序", MaterialGroup.getSort());
            map.put("创建时间", MaterialGroup.getGmtCreate());
            map.put("修改时间", MaterialGroup.getGmtUpdated());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(MaterialGroup resources) {
        this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MaterialGroup resources) {
        this.updateById(resources);
    }

    @Override
    public void deleteAll(Set<Integer> ids) {
        for (Integer id : ids) {
            //查询下面是否存在资讯
            Integer group_type = materialService.query().eq("group_type", id).count();
            if (group_type > 0) {
                throw new BadRequestException("该分组下面存在资讯不能删除");
            }
        }
        this.removeByIds(ids);
    }
}
