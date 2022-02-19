/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qkbus.modules.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码配置枚举
 *
 * @author: liaojinlong
 * @date: 2020/6/10 17:40
 */
@Getter
@AllArgsConstructor
public enum LoginCodeEnum {


    /**
     * 算数
     */
    ARITHMETIC("arithmetic", "算数"),
    /**
     * 中文
     */
    CHINESE("chinese", "中文"),
    /**
     * 中文闪图
     */

    CHINESE_GIF("chinese_gif", "中文闪图"),
    /**
     * 闪图
     */
    GIF("gif", "闪图"),

    SPEC("spec", ""),
    ;

    private final String type;

    private final String name;
}
