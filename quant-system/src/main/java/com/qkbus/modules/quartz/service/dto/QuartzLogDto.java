
package com.qkbus.modules.quartz.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Data
public class QuartzLogDto implements Serializable {

    /**
     * 定时任务名称
     */
    private String baenName;

    /**
     * Bean名称
     */
    private Timestamp gmtCreate;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 异常详细
     */
    private String exceptionDetail;

    /**
     * 状态
     */
    private Boolean isSuccess;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * 耗时（毫秒）
     */
    private Long time;
}
