
package com.qkbus.modules.system.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.modules.system.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */

public interface SysUserMapper extends CoreMapper<User> {

    /**
     * 修改密码
     *
     * @param username              用户名
     * @param password              密码
     * @param lastPasswordResetTime /
     */
    @Update("update sys_user set password = #{password} , last_password_reset_time = #{lastPasswordResetTime} where username = #{username}")
    void updatePass(@Param("password") String password, @Param("lastPasswordResetTime") String lastPasswordResetTime, @Param("username") String username);

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */
    @Update("update sys_user set email = #{email} where username = #{username}")
    void updateEmail(@Param("email") String email, @Param("username") String username);


}
