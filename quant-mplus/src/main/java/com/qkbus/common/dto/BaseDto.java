package com.qkbus.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * @author Zheng Jie
 * @date 2019年10月24日20:48:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseDto implements Serializable {


    @ApiModelProperty(value = "创建时间")
    private Timestamp gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp gmtUpdated;

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }
}
