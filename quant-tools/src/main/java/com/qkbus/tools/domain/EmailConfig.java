
package com.qkbus.tools.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */

@Data
@TableName("tools_email_config")
public class EmailConfig implements Serializable {

    /**
     * ID
     */
    @TableId
    private Long id;


    /**
     * 签名
     */
    private String sign;


    /**
     * 收件人
     */
    private String fromUser;


    /**
     * 邮件服务器SMTP地址
     */
    private String host;


    /**
     * 密码
     */
    private String pass;


    /**
     * 端口
     */
    private String port;


    /**
     * 发件者用户名
     */
    private String user;


    public void copy(EmailConfig source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
