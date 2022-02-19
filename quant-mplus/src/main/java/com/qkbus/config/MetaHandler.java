
package com.qkbus.config;

/*
 *
 * @author ：少林一枝花
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Slf4j
@Component
public class MetaHandler implements MetaObjectHandler {


    /**
     * 新增数据
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            if (metaObject.hasSetter("gmtCreate")) {
                log.debug("自动插入 gmtCreate");
                this.setFieldValByName("gmtCreate", time, metaObject);
            }
            if (metaObject.hasSetter("gmtUpdated")) {
                log.debug("自动插入 gmtUpdated");
                this.setFieldValByName("gmtUpdated", time, metaObject);
            }
            if (metaObject.hasSetter("delFlag")) {
                log.debug("自动插入 delFlag");
                this.setFieldValByName("delFlag", false, metaObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加自动注入失败:{}", e.getMessage());
        }
    }

    /**
     * 更新数据执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            if (metaObject.hasSetter("gmtUpdated")) {
                log.debug("自动插入 gmtUpdated");
                this.setFieldValByName("gmtUpdated", time, metaObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改自动注入失败:{}", e.getMessage());
        }
    }

}
