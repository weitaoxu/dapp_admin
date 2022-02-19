
package com.qkbus.tools.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.tools.domain.AlipayConfig;
import com.qkbus.tools.domain.vo.TradeVo;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
public interface AlipayConfigService extends BaseService<AlipayConfig> {

    /**
     * 处理来自PC的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade  交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsPc(AlipayConfig alipay, TradeVo trade) throws Exception;

    /**
     * 处理来自手机网页的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade  交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsWeb(AlipayConfig alipay, TradeVo trade) throws Exception;

    /**
     * 查询配置
     *
     * @return AlipayConfig
     */
    AlipayConfig find();

    /**
     * 更新配置
     *
     * @param alipayConfig 支付宝配置
     * @return AlipayConfig
     */
    void update(AlipayConfig alipayConfig);
}
