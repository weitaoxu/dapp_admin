
package com.qkbus.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.exception.EntityExistException;
import com.qkbus.modules.system.domain.*;
import com.qkbus.modules.system.service.RoleService;
import com.qkbus.modules.system.service.RolesDeptsService;
import com.qkbus.modules.system.service.RolesMenusService;
import com.qkbus.modules.system.service.UsersRolesService;
import com.qkbus.modules.system.service.dto.RoleDto;
import com.qkbus.modules.system.service.dto.RoleQueryCriteria;
import com.qkbus.modules.system.service.dto.UserDto;
import com.qkbus.modules.system.service.mapper.DeptMapper;
import com.qkbus.modules.system.service.mapper.MenuMapper;
import com.qkbus.modules.system.service.mapper.RoleMapper;
import com.qkbus.utils.FileUtil;
import com.qkbus.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@CacheConfig(cacheNames = "role")
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    private final IGenerator generator;
    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final DeptMapper deptMapper;
    private final RolesMenusService rolesMenusService;
    private final RolesDeptsService rolesDeptsService;
    private final UsersRolesService usersRolesService;

    @Override
    public Map<String, Object> queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        //只查询等级比我大的
        //获取我的等级
        Integer levels = usersRolesService.getLevels(null);
        criteria.setLevel(levels);
        getPage(pageable);
        PageInfo<Role> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), RoleDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }

    /**
     * 查询数据分页
     *
     * @param pageable 分页参数
     * @return Object
     */
    @Override
    public List<Role> queryAlls(RoleQueryCriteria criteria, Pageable pageable) {
        //只查询等级比我大的
        //获取我的等级
        Integer levels = usersRolesService.getLevels(null);
        criteria.setLevel(levels);
        getPage(pageable);
        List<Role> roleList = this.list(QueryHelpPlus.getPredicate(Role.class, criteria));
        return roleList;
    }

    @Override
    public List<Role> queryAll(RoleQueryCriteria criteria) {
        List<Role> roleList = this.list(QueryHelpPlus.getPredicate(Role.class, criteria));
        for (Role role : roleList) {
            role.setMenus(menuMapper.findMenuByRoleId(role.getId()));
            role.setDepts(deptMapper.findDeptByRoleId(role.getId()));
        }
        return roleList;
    }


    @Override
    public void download(List<RoleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDto role : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", role.getName());
            map.put("备注", role.getRemark());
            map.put("数据权限", role.getDataScope());
            map.put("角色级别", role.getLevel());
            map.put("创建日期", role.getGmtCreate());
            map.put("功能权限", role.getPermission());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 根据角色查询角色级别
     *
     * @param roles /
     * @return /
     */
    @Override
    public Integer findLevelByRoles(Set<Role> roles) {
        if (!roles.isEmpty()) {
            List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            List<Role> rolesList = this.listByIds(roleIds);
            return Collections.min(rolesList.stream().map(Role::getLevel).collect(Collectors.toList()));
        }
        return null;
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    @Override
    public RoleDto findById(long id) {
        Role role = this.getById(id);
        role.setMenus(menuMapper.findMenuByRoleId(role.getId()));
        role.setDepts(deptMapper.findDeptByRoleId(role.getId()));
        return generator.convert(role, RoleDto.class);
    }

    /**
     * 修改绑定的菜单
     *
     * @param resources /
     */
    @Override
    @CacheEvict(value = {"menu", "role"}, allEntries = true)
    public void updateMenu(Role resources) {
        //获取要修改角色的等级
        RoleDto role = this.findById(resources.getId());

        usersRolesService.getLevels(resources.getLevel());

        if (resources.getMenus().size() > 0) {
            List<RolesMenus> rolesMenusList = resources.getMenus().stream().map(i -> {
                RolesMenus rolesMenus = new RolesMenus();
                rolesMenus.setRoleId(resources.getId());
                rolesMenus.setMenuId(i.getId());
                return rolesMenus;
            }).collect(Collectors.toList());
            rolesMenusService.remove(new LambdaQueryWrapper<RolesMenus>().eq(RolesMenus::getRoleId, resources.getId()));
            rolesMenusService.saveBatch(rolesMenusList);
        }
    }


    @Override
    public RoleDto create(Role resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new role cannot already have an ID");
        }
        usersRolesService.getLevels(resources.getLevel());
        if (this.getOne(Wrappers.<Role>lambdaQuery().eq(Role::getName, resources.getName())) != null) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        if (this.getOne(new LambdaQueryWrapper<Role>().eq(Role::getName, resources.getName())) != null) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        this.save(resources);
        if (resources.getDepts().size() > 0) {
            List<RolesDepts> rolesDeptsList = resources.getDepts().stream().map(i -> {
                RolesDepts rolesDepts = new RolesDepts();
                rolesDepts.setRoleId(resources.getId());
                rolesDepts.setDeptId(i.getId());
                return rolesDepts;
            }).collect(Collectors.toList());
            rolesDeptsService.saveBatch(rolesDeptsList);
        }
        return generator.convert(resources, RoleDto.class);
    }

    @Override
    @CacheEvict(value = {"menu", "role", "userRole"}, allEntries = true)
    public void update(Role resources) {

        usersRolesService.getLevels(resources.getLevel());


        Role role = this.getById(resources.getId());

        Role role1 = this.getOne(new LambdaQueryWrapper<Role>().eq(Role::getName, resources.getName()));

        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        role1 = this.getOne(new LambdaQueryWrapper<Role>().eq(Role::getPermission, resources.getPermission()));
        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException(Role.class, "permission", resources.getPermission());
        }
        role.setName(resources.getName());
        role.setRemark(resources.getRemark());
        role.setDataScope(resources.getDataScope());
        if (resources.getDepts().size() > 0) {
            List<RolesDepts> rolesDeptsList = resources.getDepts().stream().map(i -> {
                RolesDepts rolesDepts = new RolesDepts();
                rolesDepts.setRoleId(resources.getId());
                rolesDepts.setDeptId(i.getId());
                return rolesDepts;
            }).collect(Collectors.toList());
            rolesDeptsService.remove(new LambdaQueryWrapper<RolesDepts>().eq(RolesDepts::getRoleId, resources.getId()));
            rolesDeptsService.saveBatch(rolesDeptsList);
        }
        role.setLevel(resources.getLevel());
        role.setPermission(resources.getPermission());
        this.updateById(role);
    }

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    @Override
    @Cacheable(key = "'auth:' + #p0.id")
    public Collection<GrantedAuthority> mapToGrantedAuthorities(UserDto user) {
        Set<String> permissions = new HashSet<>();
  /*      // 如果是管理员直接返回
        if (user.getIsAdmin()==1) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }*/
        Set<Role> roles = roleMapper.findByUsers_Id(user.getId());
        for (Role role : roles) {
            Set<Menu> menuSet = menuMapper.findMenuByRoleId(role.getId());
            role.setMenus(menuSet);
            Set<Dept> deptSet = deptMapper.findDeptByRoleId(role.getId());
            role.setDepts(deptSet);
        }
        permissions = roles.stream().filter(role -> StringUtils.isNotBlank(role.getPermission())).map(Role::getPermission).collect(Collectors.toSet());
        permissions.addAll(
                roles.stream().flatMap(role -> role.getMenus().stream())
                        .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                        .map(Menu::getPermission).collect(Collectors.toSet())
        );
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"menu", "role", "userRole"}, allEntries = true)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            RoleDto role = this.findById(id);
            usersRolesService.getLevels(role.getLevel());
        }
        try {
            for (Long id : ids) {
                rolesMenusService.lambdaUpdate().eq(RolesMenus::getRoleId, id).remove();
                rolesDeptsService.lambdaUpdate().eq(RolesDepts::getRoleId, id).remove();
            }
            this.removeByIds(ids);
        } catch (Throwable e) {
            throw new BadRequestException("所选角色存在用户关联，请取消关联后再试");
        }
    }


}
