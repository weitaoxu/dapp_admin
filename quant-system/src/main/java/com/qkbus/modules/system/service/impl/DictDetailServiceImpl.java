
package com.qkbus.modules.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.modules.system.domain.DictDetail;
import com.qkbus.modules.system.service.DictDetailService;
import com.qkbus.modules.system.service.dto.DictDetailDto;
import com.qkbus.modules.system.service.dto.DictDetailQueryCriteria;
import com.qkbus.modules.system.service.mapper.DictDetailMapper;
import com.qkbus.utils.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@CacheConfig(cacheNames = "DictDetail")
public class DictDetailServiceImpl extends BaseServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {

    private final IGenerator generator;

    @Override
    public Map<String, Object> queryAll(DictDetailQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DictDetail> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), DictDetailDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    @Cacheable(key = "'dictName:' +#criteria.getDictName()")
    public List<DictDetail> queryAll(DictDetailQueryCriteria criteria) {
        return baseMapper.selectDictDetailList(criteria.getLabel(), criteria.getDictName());
    }


    @Override
    public void download(List<DictDetailDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictDetailDto dictDetail : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("字典标签", dictDetail.getLabel());
            map.put("字典值", dictDetail.getValue());
            map.put("排序", dictDetail.getSort());
            map.put("字典id", dictDetail.getDictId());
            map.put("创建日期", dictDetail.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @CacheEvict(value = {"DictDetail"}, allEntries = true)
    public void create(DictDetail resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new dictDetail cannot already have an ID");
        }
        resources.setDictId(resources.getDict().getId());
        this.save(resources);
    }

    @Override
    @CacheEvict(value = {"DictDetail"}, allEntries = true)
    public void update(DictDetail resources) {
        this.updateById(resources);
    }

    @Override
    @CacheEvict(value = {"DictDetail"}, allEntries = true)
    public void delete(Long ids) {
        removeById(ids);
    }
}
