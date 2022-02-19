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
package com.qkbus.constant;

/**
 * @author: liaojinlong
 * @date: 2020/6/11 15:49
 * @apiNote: 关于缓存的Key集合
 */
public interface CacheKey {

    /**
     * 用户
     */
    String USER_ID = RedisConstant.REDIS_NAME + "user::id:";
    /**
     * 数据
     */
    String DATA_USER = RedisConstant.REDIS_NAME + "data::user:";
    /**
     * 菜单
     */
    String MENU_ID = RedisConstant.REDIS_NAME + "menu::id:";
    String MENU_USER = RedisConstant.REDIS_NAME + "menu::user:";
    /**
     * 角色授权
     */
    String ROLE_AUTH = RedisConstant.REDIS_NAME + "role::auth:";
    /**
     * 角色信息
     */
    String ROLE_ID = RedisConstant.REDIS_NAME + "role::id:";
    /**
     * 部门
     */
    String DEPT_ID = RedisConstant.REDIS_NAME + "dept::id:";
    /**
     * 岗位
     */
    String JOB_ID = RedisConstant.REDIS_NAME + "job::id:";
    /**
     * 数据字典
     */
    String DICT_NAME = RedisConstant.REDIS_NAME + "dict::name:";
}
