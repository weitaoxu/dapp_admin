
package com.qkbus.modules.system.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.modules.system.domain.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */

public interface RoleMapper extends CoreMapper<Role> {

    /**
     * 根据用户ID查询
     *
     * @param id 用户ID
     * @return
     */
    @Select("SELECT r.id,r.gmt_create,r.data_scope,r.`level`,r.`name`,r.permission,r.remark " +
            "FROM sys_role r LEFT OUTER JOIN sys_users_roles u1 ON r.id = u1.role_id " +
            "LEFT OUTER JOIN sys_user u2 ON u1.user_id = u2.id " +
            "WHERE u2.id = #{id}")
    Set<Role> findByUsers_Id(@Param("id") Long id);


}
