/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version loginCode.length.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-loginCode.length.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qkbus.modules.security.config;

import com.qkbus.exception.BadRequestException;
import com.qkbus.utils.StringUtils;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.awt.*;


/**
 * 配置文件读取
 */
@Data
@Component
public class LoginProperties {

    /**
     * 验证码内容长度
     */
    private int length = 2;
    /**
     * 验证码宽度
     */
    private int width = 111;
    /**
     * 验证码高度
     */
    private int height = 36;
    /**
     * 验证码字体
     */
    private String fontName;
    /**
     * 字体大小
     */
    private int fontSize = 25;

    /**
     * 获取验证码生产类
     *
     * @return /
     */
    public Captcha getCaptcha() {
        return switchCaptcha(LoginCodeEnum.ARITHMETIC);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param loginCode 验证码配置信息
     * @return /
     */
    private Captcha switchCaptcha(LoginCodeEnum loginCode) {
        Captcha captcha;
        synchronized (this) {
            switch (loginCode) {
                case ARITHMETIC:
                    // 算术类型 https://gitee.com/whvse/EasyCaptcha
                    captcha = new ArithmeticCaptcha();
                    break;
                case CHINESE:
                    captcha = new ChineseCaptcha();
                    break;
                case CHINESE_GIF:
                    captcha = new ChineseGifCaptcha();
                    break;
                case GIF:
                    captcha = new GifCaptcha();
                    break;
                case SPEC:
                    captcha = new SpecCaptcha();
                    break;
                default:
                    throw new BadRequestException("验证码配置信息错误！正确配置查看");
            }
            captcha.setHeight(height);
            captcha.setWidth(width);
            captcha.setLen(length);
        }
        if (StringUtils.isNotBlank(fontName)) {
            captcha.setFont(new Font(fontName, Font.PLAIN, fontSize));
        }
        return captcha;
    }


}
