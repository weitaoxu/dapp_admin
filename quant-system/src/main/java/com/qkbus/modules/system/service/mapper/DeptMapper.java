
package com.qkbus.modules.system.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.modules.system.domain.Dept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */

public interface DeptMapper extends CoreMapper<Dept> {

    @Select("select m.* from sys_dept m LEFT JOIN sys_roles_depts t on m.id= t.dept_id LEFT JOIN sys_role r on r.id = t.role_id where r.id = ${roleId}")
    Set<Dept> findDeptByRoleId(@Param("roleId") Long roleId);

    @Select("select * from sys_dept m LEFT JOIN sys_roles_depts t on m.id= t.dept_id LEFT JOIN sys_role r on r.id = t.role_id where r.id = #{roleId}")
    Set<Dept> findDeptByRoleIds(@Param("roleIds") Set<Long> roleId);
}
