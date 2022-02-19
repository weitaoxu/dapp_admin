
package com.qkbus.modules.system.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.modules.system.domain.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */

public interface MenuMapper extends CoreMapper<Menu> {


    @Select("select m.* from sys_menu m LEFT JOIN sys_roles_menus t on m.id= t.menu_id LEFT JOIN sys_role r on r.id = t.role_id where r.id = #{roleId}")
    Set<Menu> findMenuByRoleId(@Param("roleId") Long roleId);

    @Select("<script>select m.* from sys_menu m LEFT OUTER JOIN sys_roles_menus t on m.id= t.menu_id LEFT OUTER JOIN sys_role r on r.id = t.role_id where m.type!=2 and  r.id in <foreach collection=\"roleIds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach> order by m.sort asc</script>")
    List<Menu> selectListByRoles(@Param("roleIds") Set<Long> roleIds);

}
