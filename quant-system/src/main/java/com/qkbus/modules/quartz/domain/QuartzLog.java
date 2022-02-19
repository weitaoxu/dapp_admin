
package com.qkbus.modules.quartz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qkbus.common.entity.BaseEntity;
import lombok.Data;


/**
 * @author 少林一枝花
 * @date 2020-05-13
 */

@Data
@TableName("sys_quartz_log")
public class QuartzLog extends BaseEntity {

    /**
     * 任务日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 任务名称
     */
    private String baenName;


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


    public void copy(QuartzLog source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
