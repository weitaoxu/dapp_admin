
package com.qkbus.modules.quartz.rest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qkbus.aop.ForbidSubmit;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.quartz.domain.QuartzJob;
import com.qkbus.modules.quartz.service.QuartzJobService;
import com.qkbus.modules.quartz.service.QuartzLogService;
import com.qkbus.modules.quartz.service.dto.QuartzJobDto;
import com.qkbus.modules.quartz.service.dto.QuartzJobQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2019-01-07
 */
@Slf4j
@RestController
@Api(tags = "系统:定时任务管理")
@RequestMapping("/api/jobs")
public class QuartzJobController {


    private final QuartzJobService quartzJobService;
    private final IGenerator generator;
    private final QuartzLogService quartzLogService;


    public QuartzJobController(QuartzJobService quartzJobService, IGenerator generator, QuartzLogService quartzLogService) {
        this.quartzJobService = quartzJobService;
        this.generator = generator;
        this.quartzLogService = quartzLogService;
    }

    @Log("查询定时任务")
    @ApiOperation("查询定时任务")
    @GetMapping
    @PreAuthorize("@el.check('admin','timing:list')")
    public ResponseEntity<Object> getJobs(QuartzJobQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(quartzJobService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("导出任务数据")
    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','timing:list')")
    public void download(HttpServletResponse response, QuartzJobQueryCriteria criteria) throws IOException {
        quartzJobService.download(generator.convert(quartzJobService.queryAll(criteria), QuartzJobDto.class), response);
    }


    @ForbidSubmit
    @Log("新增定时任务")
    @ApiOperation("新增定时任务")
    @PostMapping
    @PreAuthorize("@el.check('admin','timing:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody QuartzJob resources) {
        quartzJobService.createJob(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ForbidSubmit
    @Log("修改定时任务")
    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('admin','timing:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody QuartzJob resources) {
        quartzJobService.updateJob(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("更改定时任务状态")
    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@el.check('admin','timing:edit')")
    public ResponseEntity<Object> updateIsPause(@PathVariable Long id) {
        quartzJobService.updateIsPause(quartzJobService.getById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("执行定时任务")
    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
    @PreAuthorize("@el.check('admin','timing:edit')")
    public ResponseEntity<Object> execution(@PathVariable Long id) {
        quartzJobService.execution(quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getId, id)));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除定时任务")
    @ApiOperation("删除定时任务")
    @DeleteMapping
    @PreAuthorize("@el.check('admin','timing:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        quartzJobService.deleteJob(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
