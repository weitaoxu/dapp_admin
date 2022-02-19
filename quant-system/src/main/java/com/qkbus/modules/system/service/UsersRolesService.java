
package com.qkbus.modules.system.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.system.domain.Role;
import com.qkbus.modules.system.domain.UsersRoles;
import com.qkbus.modules.system.service.dto.RoleSmallDto;

import java.util.List;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-16
 */
public interface UsersRolesService extends BaseService<UsersRoles> {


    /*
     *
     *保存用户角色
     * */
    void saveUserRole(Long userId, Set<Role> roles);


    List<RoleSmallDto> findRoleByUsersId(Long userId);


    //获取我的等级和判断等级
    Integer getLevels(Integer level);


}
