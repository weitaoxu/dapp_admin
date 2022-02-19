package com.qkbus.constant;

public interface RedisConstant {

    /**
     * 默认超时时间，单位秒
     */
    int DEFAULT_TIME_OUT = 60 * 60 * 24 * 7;
    //冻结时间
    int FREEZE_ADMIN_LOCK = 30 * 60;

    int LOCK_TIME_OUT = 10;


    String REDIS_NAME = "quant_admin:";

    String SYMBOL_MARKET = "symbol_market";


    String QUANT = "quant:";
    String QKBUS = "qkbus:";
    /*
     * 冻结admin
     * */
    String FREEZE_ADMIN = REDIS_NAME + "freeze_admin:";

    /*
     * 冻结admin  次数 保存一天  凌晨释放
     * */
    String FREEZE_ADMIN_NUM = REDIS_NAME + "freeze_admin_num:";

    /*
     * 输入admin次数
     * */
    String ADMIN_LOGIN_NUM = REDIS_NAME + "admin_login_num:";


    /*
     * 输入admin次数
     * */
    String ADMIN_LOGIN_IP = REDIS_NAME + "admin_login_ip:";


    String MARKET_UPDATE = QKBUS + "market_update";

    /**
     * Redis的系统名称
     */
    String SYSTEM_NAME = "qkbus_quant";

    String CHECK_WITHDRAW_LOCK = REDIS_NAME + "check_withdraw_lock:";

    String WITHDRAW_ROBOT_REVENUE = SYSTEM_NAME + ":withdraw_robot_revenue";

    String ACCOUNT_LOCK = SYSTEM_NAME + ":accountLock:";

    /**
     * 系统全局配置
     */
    String SYSTEM_CONFIG = SYSTEM_NAME + ":systemConfig";


    String ROBOT_CONFIG = SYSTEM_NAME + ":robot_config:";


    String CHECK_TRADE_LOCK = REDIS_NAME + "CHECK_TRADE_LOCK:";
}
