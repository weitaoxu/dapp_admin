
package com.qkbus.modules.system.service.impl;

import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.modules.system.domain.Role;
import com.qkbus.modules.system.domain.UsersRoles;
import com.qkbus.modules.system.service.UsersRolesService;
import com.qkbus.modules.system.service.dto.RoleSmallDto;
import com.qkbus.modules.system.service.mapper.RoleMapper;
import com.qkbus.modules.system.service.mapper.UsersRolesMapper;
import com.qkbus.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 少林一枝花
 * @date 2020-05-16
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
@CacheConfig(cacheNames = "userRole")
public class UsersRolesServiceImpl extends BaseServiceImpl<UsersRolesMapper, UsersRoles> implements UsersRolesService {


    private final IGenerator generator;
    private final RoleMapper roleMapper;

    @Override
    public void saveUserRole(Long userId, Set<Role> roles) {
        //添加之前清除原来的角色
        this.lambdaUpdate().eq(UsersRoles::getUserId, userId).remove();
        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setUserId(userId);
        for (Role roleIds : roles) {
            usersRoles.setRoleId(roleIds.getId());
            boolean save = this.save(usersRoles);
            if (!save) {
                log.error("添加用户角色失败===");
                throw new BadRequestException("网络异常");
            }
        }
    }

    @Override
    @Cacheable(key = "'level:' + #userId")
    public List<RoleSmallDto> findRoleByUsersId(Long userId) {
        List<Role> roles = new ArrayList<>();
        List<UsersRoles> user_id = this.query().eq("user_id", userId).list();
        if (!user_id.isEmpty()) {
            Set<Long> collect = user_id.stream().map(UsersRoles::getRoleId).collect(Collectors.toSet());
            if (!collect.isEmpty()) {
                roles = roleMapper.selectBatchIds(collect);
            }
        }
        return generator.convert(roles, RoleSmallDto.class);
    }


    @Override
    public Integer getLevels(Integer level) {
        List<RoleSmallDto> roles = this.findRoleByUsersId(SecurityUtils.getUserId());
        List<Integer> levels = roles.stream().map(RoleSmallDto::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            if (level < min) {
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }

}
