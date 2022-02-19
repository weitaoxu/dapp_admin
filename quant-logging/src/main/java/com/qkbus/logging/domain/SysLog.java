
package com.qkbus.logging.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qkbus.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 少林一枝花
 * @date 2021-11-24
 */
@Data
@TableName("sys_log")
@NoArgsConstructor
public class SysLog extends BaseEntity {

    @TableId
    private Long id;

    /**
     * 操作用户
     */
    private String username;

    @TableField(exist = false)
    private String nickname;

    /**
     * 描述
     */
    private String description;

    /**
     * 方法名
     */
    private String method;

    private Long uid;

    //类型 0-后台 1-前台
    private Integer type;

    /**
     * 参数
     */
    private String params;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 地址
     */
    private String address;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 请求耗时
     */
    private Long time;

    /**
     * 异常详细
     */
    private byte[] exceptionDetail;

    @TableLogic
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;

    public SysLog(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
