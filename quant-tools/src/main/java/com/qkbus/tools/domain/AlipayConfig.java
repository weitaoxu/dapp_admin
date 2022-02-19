
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
@TableName("tools_alipay_config")
public class AlipayConfig implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;


    /**
     * 应用ID
     */
    private String appId;


    /**
     * 编码
     */
    private String charset;


    /**
     * 类型 固定格式json
     */
    private String format;


    /**
     * 网关地址
     */
    private String gatewayUrl;


    /**
     * 异步回调
     */
    private String notifyUrl;


    /**
     * 私钥
     */
    private String privateKey;


    /**
     * 公钥
     */
    private String publicKey;


    /**
     * 回调地址
     */
    private String returnUrl;


    /**
     * 签名方式
     */
    private String signType;


    /**
     * 商户号
     */
    private String sysServiceProviderId;


    public void copy(AlipayConfig source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
