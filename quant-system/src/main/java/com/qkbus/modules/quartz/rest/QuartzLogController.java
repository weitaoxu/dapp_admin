
package com.qkbus.modules.quartz.rest;

import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.quartz.service.QuartzLogService;
import com.qkbus.modules.quartz.service.dto.QuartzLogDto;
import com.qkbus.modules.quartz.service.dto.QuartzLogQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 少林一枝花
 * @date 2019-01-07
 */
@Slf4j
@RestController
@Api(tags = "系统:定时任务管理")
@RequestMapping("/api/jobs")
@AllArgsConstructor
public class QuartzLogController {

    private final IGenerator generator;
    private final QuartzLogService quartzLogService;


    @Log("导出日志数据")
    @ApiOperation("导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@el.check('admin','timing:list')")
    public void downloadLog(HttpServletResponse response, QuartzLogQueryCriteria criteria) throws IOException {
        quartzLogService.download(generator.convert(quartzLogService.queryAll(criteria), QuartzLogDto.class), response);
    }

    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
    @PreAuthorize("@el.check('admin','timing:list')")
    public ResponseEntity<Object> getJobLogs(QuartzLogQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(quartzLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

}
