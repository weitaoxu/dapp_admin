
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
@TableName("sys_quartz_job")
public class QuartzJob extends BaseEntity {

    public static final String JOB_KEY = "JOB_KEY";

    /**
     * 定时任务ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * Spring Bean名称
     */
    private String beanName;


    /**
     * cron 表达式
     */
    private String cronExpression;


    /**
     * 状态：1暂停、0启用
     */
    private Boolean isPause;


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
     * 备注
     */
    private String remark;

    /*
     * 失败告警邮箱
     * */
    private String email;

    /*
     *失败是否暂停
     * */
    private Boolean pauseAfterFailure;


    public void copy(QuartzJob source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
