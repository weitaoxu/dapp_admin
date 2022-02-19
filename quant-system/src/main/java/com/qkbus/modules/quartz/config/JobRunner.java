
package com.qkbus.modules.quartz.config;

import com.qkbus.modules.quartz.domain.QuartzJob;
import com.qkbus.modules.quartz.service.QuartzJobService;
import com.qkbus.modules.quartz.utils.QuartzManage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 少林一枝花
 * @date 2019-01-07
 */
@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {

    private final QuartzJobService quartzJobService;

    private final QuartzManage quartzManage;


    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        System.out.println("--------------------注入定时任务---------------------");
        List<QuartzJob> quartzJobs = quartzJobService.findByIsPauseIsFalse();
        quartzJobs.forEach(quartzManage::addJob);
        System.out.println("--------------------定时任务注入完成---------------------");
    }
}
