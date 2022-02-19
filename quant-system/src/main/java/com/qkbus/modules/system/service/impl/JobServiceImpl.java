
package com.qkbus.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.modules.system.domain.Job;
import com.qkbus.modules.system.service.JobService;
import com.qkbus.modules.system.service.dto.JobDto;
import com.qkbus.modules.system.service.dto.JobQueryCriteria;
import com.qkbus.modules.system.service.mapper.DeptMapper;
import com.qkbus.modules.system.service.mapper.JobMapper;
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
 * @date 2020-05-14
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl extends BaseServiceImpl<JobMapper, Job> implements JobService {

    private final IGenerator generator;

    private final DeptMapper deptMapper;

    @Override
    public Map<String, Object> queryAll(JobQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Job> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), JobDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<Job> queryAll(JobQueryCriteria criteria) {
        QueryWrapper predicate = QueryHelpPlus.getPredicate(Job.class, criteria);
        List<Job> jobList = this.list(predicate);
        if (criteria.getDeptIds().size() == 0) {
            for (Job job : jobList) {
                job.setDept(deptMapper.selectById(job.getDeptId()));
            }
        } else {
            //断权限范围
            for (Long deptId : criteria.getDeptIds()) {
                for (Job job : jobList) {
                    if (deptId.equals(job.getDeptId())) {
                        job.setDept(deptMapper.selectById(job.getDeptId()));
                    }
                }
            }
        }
        return jobList;
    }


    @Override
    public void download(List<JobDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobDto job : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", job.getName());
            map.put("岗位状态", job.getEnabled());
            map.put("岗位排序", job.getSort());
            map.put("创建日期", job.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
