
package com.qkbus.modules.material.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.modules.material.domain.Notice;
import com.qkbus.modules.material.service.NoticeService;
import com.qkbus.modules.material.service.dto.NoticeDto;
import com.qkbus.modules.material.service.dto.NoticeQueryCriteria;
import com.qkbus.modules.material.service.mapper.NoticeMapper;
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
 * @date 2021-05-14
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, Notice> implements NoticeService {

    private final IGenerator generator;

    @Override
    public Map<String, Object> queryAll(NoticeQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Notice> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), NoticeDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<Notice> queryAll(NoticeQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(Notice.class, criteria));
    }


    @Override
    public void download(List<NoticeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (NoticeDto Notice : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("标题", Notice.getTitle());
            map.put("内容", Notice.getContent());
            map.put("类型", Notice.getNoticeType());
            map.put("排序", Notice.getSort());
            map.put("状态", Notice.getStatus());
            map.put("创建时间", Notice.getGmtCreate());
            map.put("修改时间", Notice.getGmtUpdated());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Notice resources) {
        this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Notice resources) {
        this.updateById(resources);
    }

    @Override
    public void deleteAll(Set<Long> ids) {
        this.removeByIds(ids);
    }
}
