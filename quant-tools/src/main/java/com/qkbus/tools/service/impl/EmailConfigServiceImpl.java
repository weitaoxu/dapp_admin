
package com.qkbus.tools.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.tools.domain.EmailConfig;
import com.qkbus.tools.domain.vo.EmailVo;
import com.qkbus.tools.service.EmailConfigService;
import com.qkbus.tools.service.mapper.EmailConfigMapper;
import com.qkbus.utils.EncryptUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmailConfigServiceImpl extends BaseServiceImpl<EmailConfigMapper, EmailConfig> implements EmailConfigService {

    private final IGenerator generator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EmailConfig emailConfig, EmailConfig old) {
        try {
            if (!emailConfig.getPass().equals(old.getPass())) {
                // 对称加密
                emailConfig.setPass(EncryptUtils.desEncrypt(emailConfig.getPass()));
            } else {
                throw new BadRequestException("新旧密码一致");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.save(emailConfig);
    }

    @Override
    public EmailConfig find() {
        EmailConfig emailConfig = this.getOne(new QueryWrapper<>());
        return emailConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(EmailVo emailVo, EmailConfig emailConfig) {
        if (emailConfig == null) {
            throw new BadRequestException("请先配置，再操作");
        }
        // 封装
        MailAccount account = new MailAccount();
        account.setHost(emailConfig.getHost());
        account.setPort(Integer.parseInt(emailConfig.getPort()));
        account.setAuth(true);
        try {
            // 对称解密
            account.setPass(EncryptUtils.desDecrypt(emailConfig.getPass()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        account.setUser(emailConfig.getUser());
        account.setFrom(emailConfig.getSign() + "<" + emailConfig.getFromUser() + ">");
        // ssl方式发送
        account.setSslEnable(true);
        // 使用STARTTLS安全连接
        account.setStarttlsEnable(true);
        String content = emailVo.getContent();
        // 发送
        try {
            int size = emailVo.getTos().size();
            Mail.create(account)
                    .setTos(emailVo.getTos().toArray(new String[size]))
                    .setTitle("【" + emailConfig.getSign() + "】" + emailVo.getSubject())
                    .setContent(content)
                    .setHtml(true)
                    //关闭session
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
