
package com.qkbus.modules.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.modules.system.domain.Dict;
import com.qkbus.modules.system.domain.DictDetail;
import com.qkbus.modules.system.service.DictDetailService;
import com.qkbus.modules.system.service.DictService;
import com.qkbus.modules.system.service.dto.DictDto;
import com.qkbus.modules.system.service.dto.DictQueryCriteria;
import com.qkbus.modules.system.service.mapper.DictMapper;
import com.qkbus.utils.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
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
 * @date 2020-05-14
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@CacheConfig(cacheNames = "dict")
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements DictService {

    private final IGenerator generator;

    private final DictDetailService dictDetailService;

    @Override
    public Map<String, Object> queryAll(DictQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Dict> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), DictDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<Dict> queryAll(DictQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(Dict.class, criteria));
    }


    @Override
    public void download(List<DictDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictDto dict : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("字典名称", dict.getName());
            map.put("描述", dict.getRemark());
            map.put("创建日期", dict.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void create(Dict resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new dict cannot already have an ID");
        }
        this.save(resources);
    }

    @Override
    @CacheEvict(value = {"DictDetail"}, allEntries = true)
    public void update(Dict resources) {
        this.updateById(resources);
    }

    @Override
    @CacheEvict(value = {"DictDetail"}, allEntries = true)
    public void delete(Set<Long> ids) {
        //查询子类
        List<DictDetail> dict_id = dictDetailService.query().in("dict_id", ids).list();
        List<Long> collect = dict_id.stream().map(DictDetail::getId).collect(Collectors.toList());
        //删除子类
        dictDetailService.removeByIds(collect);
        this.removeByIds(ids);
    }
}
