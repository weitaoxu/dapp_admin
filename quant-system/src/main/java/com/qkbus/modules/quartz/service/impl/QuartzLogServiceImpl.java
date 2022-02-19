
package com.qkbus.modules.quartz.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.modules.quartz.domain.QuartzLog;
import com.qkbus.modules.quartz.service.QuartzLogService;
import com.qkbus.modules.quartz.service.dto.QuartzLogDto;
import com.qkbus.modules.quartz.service.dto.QuartzLogQueryCriteria;
import com.qkbus.modules.quartz.service.mapper.QuartzLogMapper;
import com.qkbus.utils.FileUtil;
import lombok.AllArgsConstructor;
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
 * @date 2020-05-13
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QuartzLogServiceImpl extends BaseServiceImpl<QuartzLogMapper, QuartzLog> implements QuartzLogService {

    private final IGenerator generator;

    @Override
    public Map<String, Object> queryAll(QuartzLogQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<QuartzLog> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), QuartzLogDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<QuartzLog> queryAll(QuartzLogQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(QuartzLog.class, criteria));
    }

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    @Override
    public void download(List<QuartzLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QuartzLogDto quartzLog : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务", quartzLog.getBaenName());
            map.put("Bean名称 ", quartzLog.getGmtCreate());
            map.put("cron表达式", quartzLog.getCronExpression());
            map.put("异常详细 ", quartzLog.getExceptionDetail());
            map.put("状态", quartzLog.getIsSuccess());
            map.put("任务名称", quartzLog.getJobName());
            map.put("方法名称", quartzLog.getMethodName());
            map.put("参数", quartzLog.getParams());
            map.put("耗时（毫秒）", quartzLog.getTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
