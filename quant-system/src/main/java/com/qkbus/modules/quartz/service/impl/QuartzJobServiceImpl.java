
package com.qkbus.modules.quartz.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.modules.quartz.domain.QuartzJob;
import com.qkbus.modules.quartz.service.QuartzJobService;
import com.qkbus.modules.quartz.service.dto.QuartzJobDto;
import com.qkbus.modules.quartz.service.dto.QuartzJobQueryCriteria;
import com.qkbus.modules.quartz.service.mapper.QuartzJobMapper;
import com.qkbus.modules.quartz.utils.QuartzManage;
import com.qkbus.utils.FileUtil;
import lombok.AllArgsConstructor;
import org.quartz.CronExpression;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QuartzJobServiceImpl extends BaseServiceImpl<QuartzJobMapper, QuartzJob> implements QuartzJobService {

    private final IGenerator generator;
    private final QuartzManage quartzManage;

    @Override

    public Map<String, Object> queryAll(QuartzJobQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<QuartzJob> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), QuartzJobDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override

    public List<QuartzJob> queryAll(QuartzJobQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(QuartzJob.class, criteria));
    }


    @Override
    public void download(List<QuartzJobDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QuartzJobDto quartzJob : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", quartzJob.getBeanName());
            map.put("cron 表达式", quartzJob.getCronExpression());
            map.put("状态：1暂停、0启用", quartzJob.getIsPause());
            map.put("任务名称", quartzJob.getJobName());
            map.put("方法名称", quartzJob.getMethodName());
            map.put("参数", quartzJob.getParams());
            map.put("备注", quartzJob.getRemark());
            map.put("创建日期", quartzJob.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 更改定时任务状态
     *
     * @param quartzJob /
     */
    @Override
    public void updateIsPause(QuartzJob quartzJob) {
        if (quartzJob.getIsPause()) {
            quartzManage.resumeJob(quartzJob);
        } else {
            quartzManage.pauseJob(quartzJob);
        }
        quartzJob.setIsPause(!quartzJob.getIsPause());
        this.saveOrUpdate(quartzJob);
    }


    /**
     * 立即执行定时任务
     *
     * @param quartzJob /
     */
    @Override
    public void execution(QuartzJob quartzJob) {
        quartzManage.runJobNow(quartzJob);
    }

    /**
     * 查询启用的任务
     *
     * @return List
     */
    @Override
    public List<QuartzJob> findByIsPauseIsFalse() {
        QuartzJobQueryCriteria criteria = new QuartzJobQueryCriteria();
        criteria.setIsPause(false);
        return baseMapper.selectList(QueryHelpPlus.getPredicate(QuartzJob.class, criteria));
    }

    @Override
    public void deleteJob(Set<Long> ids) {
        ids.forEach(id -> {
            QuartzJob quartzJob = baseMapper.selectById(id);
            quartzManage.deleteJob(quartzJob);
        });
        this.removeByIds(ids);
    }

    @Override
    public void createJob(QuartzJob resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new quartzJob cannot already have an ID");
        }
        if (!CronExpression.isValidExpression(resources.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        this.save(resources);
        quartzManage.addJob(resources);
    }

    @Override
    public void updateJob(QuartzJob resources) {
        if (!CronExpression.isValidExpression(resources.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        this.updateById(resources);
        quartzManage.updateJobCron(resources);

    }
}
