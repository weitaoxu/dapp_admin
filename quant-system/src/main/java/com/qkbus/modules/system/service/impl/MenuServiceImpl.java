
package com.qkbus.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.exception.EntityExistException;
import com.qkbus.modules.system.domain.Menu;
import com.qkbus.modules.system.domain.User;
import com.qkbus.modules.system.domain.vo.MenuMetaVo;
import com.qkbus.modules.system.domain.vo.MenuVo;
import com.qkbus.modules.system.service.MenuService;
import com.qkbus.modules.system.service.RoleService;
import com.qkbus.modules.system.service.RolesMenusService;
import com.qkbus.modules.system.service.UsersRolesService;
import com.qkbus.modules.system.service.dto.MenuDto;
import com.qkbus.modules.system.service.dto.MenuQueryCriteria;
import com.qkbus.modules.system.service.dto.RoleSmallDto;
import com.qkbus.modules.system.service.mapper.MenuMapper;
import com.qkbus.modules.system.service.mapper.RoleMapper;
import com.qkbus.modules.system.service.mapper.SysUserMapper;
import com.qkbus.utils.FileUtil;
import com.qkbus.utils.SecurityUtils;
import com.qkbus.utils.StringUtils;
import com.qkbus.utils.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
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
@CacheConfig(cacheNames = "menu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

    private final IGenerator generator;
    private final MenuMapper menuMapper;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final SysUserMapper sysUserMapper;
    private final UsersRolesService usersRolesService;

    private final RolesMenusService rolesMenusService;


    @Override
    public Map<String, Object> queryAll(MenuQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Menu> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), MenuDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<Menu> queryAll(MenuQueryCriteria criteria) {
        QueryWrapper predicate = QueryHelpPlus.getPredicate(Menu.class, criteria);

        return baseMapper.selectList(predicate);
    }


    @Override
    public void download(List<MenuDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MenuDto menu : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("是否外链", menu.getIFrame());
            map.put("菜单名称", menu.getName());
            map.put("组件", menu.getComponent());
            map.put("上级菜单ID", menu.getPid());
            map.put("排序", menu.getSort());
            map.put("图标", menu.getIcon());
            map.put("链接地址", menu.getPath());
            map.put("缓存", menu.getCache());
            map.put("是否隐藏", menu.getHidden());
            map.put("组件名称", menu.getComponentName());
            map.put("创建日期", menu.getGmtCreate());
            map.put("权限", menu.getPermission());
            map.put("类型", menu.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 构建菜单树
     *
     * @param menuDtos 原始数据
     * @return /
     */
    @Override
    public Map<String, Object> buildTree(List<MenuDto> menuDtos) {
        List<MenuDto> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDto menuDto : menuDtos) {
            if (menuDto.getPid() == 0 || menuDto.getPid() == null) {
                trees.add(menuDto);
            }
            for (MenuDto it : menuDtos) {
                if (it.getPid().equals(menuDto.getId())) {
                    if (menuDto.getChildren() == null) {
                        menuDto.setChildren(new ArrayList<>());
                    }
                    menuDto.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        Map<String, Object> map = new HashMap<>(2);
        if (trees.size() == 0) {
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        map.put("content", trees);
        map.put("totalElements", menuDtos.size());
        return map;
    }

    /**
     * 构建菜单树
     *
     * @param menuDtos /
     * @return /
     */
    @Override
    public List<MenuVo> buildMenus(List<MenuDto> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        List<MenuDto> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName()) ? menuDTO.getComponentName() : menuDTO.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == 0 ? "/" + menuDTO.getPath() : menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if (!menuDTO.getIFrame()) {
                            if (menuDTO.getPid() == 0) {
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                            } else if (menuDTO.getPid() != null && menuDTO.getType() == 0) {
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "ParentView" : menuDTO.getComponent());
                            } else if (!StrUtil.isEmpty(menuDTO.getComponent())) {
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getName(), menuDTO.getIcon(), !menuDTO.getCache()));
                        if (menuDtoList != null && menuDtoList.size() != 0) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menuDTO.getPid() == 0) {
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (!menuDTO.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }


    /**
     * 获取菜单树
     *
     * @return /
     */
    @Override
    public Object getMenuTree() {
        //获取用户角色并获取对应的权限
        User user = sysUserMapper.selectById(SecurityUtils.getUserId());
        List<MenuDto> byRoles = new ArrayList<>();
        if (user.getIsAdmin().equals(User.IS_ADMIN_YES)) {
            //获取所有的权限
            List<Menu> list = menuMapper.selectList(new QueryWrapper<>());
            byRoles = generator.convert(list, MenuDto.class);
        } else {
            //根据用户id获取角色菜单
            byRoles = this.findByUserId(SecurityUtils.getUserId());
        }
        List<Map<String, Object>> menuList = new ArrayList<>();
        for (MenuDto menu : byRoles) {
            Map<String, Object> mapMenu = new HashMap<>();
            if (menu.getPid().equals(0L)) {
                mapMenu.put("id", menu.getId());
                mapMenu.put("label", menu.getName());
                mapMenu.put("children", menuChildren(menu.getId(), byRoles));
                menuList.add(mapMenu);
            }
        }
        return menuList;
    }

    /**
     * 递归获取孩子节点
     */
    public List<Map<String, Object>> menuChildren(Long parentId, List<MenuDto> list) {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        for (MenuDto me : list) {
            Map<String, Object> childMap = new HashMap<String, Object>();
            if (parentId.equals(me.getPid())) {
                childMap.put("id", me.getId());
                childMap.put("label", me.getName());
                childMap.put("children", menuChildren(me.getId(), list));
                lists.add(childMap);
            }
        }
        return lists;
    }

    /**
     * 获取待删除的菜单
     *
     * @param menuList /
     * @param menuSet  /
     * @return /
     */
    @Override
    public Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet) {
        // 递归找出待删除的菜单
        for (Menu menu : menuList) {
            menuSet.add(menu);
            List<Menu> menus = this.query().eq("pid", menu.getId()).list();
            if (menus != null && menus.size() != 0) {
                getDeleteMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    /**
     * 根据pid查询
     *
     * @param pid /
     * @return /
     */
    @Override
    public List<Menu> findByPid(long pid) {
        return this.query().eq("pid", pid).list();
    }

    /*
     * 删除
     *
     * @param menuSet /
     */
    @Override
    @CacheEvict(value = {"menu", "role"}, allEntries = true)
    public void delete(Set<Menu> menuSet) {
        for (Menu menu : menuSet) {
            //删除菜单绑定的角色
            rolesMenusService.untiedMenu(menu.getId());
            this.removeById(menu.getId());
        }
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Override
    @CacheEvict(value = {"menu", "role"}, allEntries = true)
    public void update(Menu resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Menu menu = this.getById(resources.getId());
        ValidationUtil.isNull(menu.getId(), "Permission", "id", resources.getId());

        isExitHttp(resources);

        Menu menu1 = this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getName, resources.getName()));

        if (menu1 != null && !menu1.getId().equals(menu.getId())) {
            throw new EntityExistException(Menu.class, "name", resources.getName());
        }

        if (StringUtils.isNotBlank(resources.getComponentName())) {
            menu1 = this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getComponentName, resources.getComponentName()));
            if (menu1 != null && !menu1.getId().equals(menu.getId())) {
                throw new EntityExistException(Menu.class, "componentName", resources.getComponentName());
            }
        }

        menu.setId(resources.getId());
        menu.setName(resources.getName());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setSort(resources.getSort());
        menu.setCache(resources.getCache());
        menu.setHidden(resources.getHidden());
        menu.setComponentName(resources.getComponentName());
        menu.setPermission(resources.getPermission());
        menu.setType(resources.getType());
        this.saveOrUpdate(menu);
    }

    @Override
    @CacheEvict(value = {"menu", "role"}, allEntries = true)
    public MenuDto create(Menu resources) {
        isExitHttp(resources);
        if (this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getName, resources.getName())) != null) {
            throw new EntityExistException(Menu.class, "name", resources.getName());
        }
        if (StringUtils.isNotBlank(resources.getComponentName())) {
            if (this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getComponentName, resources.getComponentName())) != null) {
                throw new EntityExistException(Menu.class, "componentName", resources.getComponentName());
            }
        }
        this.save(resources);
        return generator.convert(resources, MenuDto.class);
    }

    /**
     * 用户角色改变时需清理缓存
     *
     * @param currentUserId /
     * @return /
     */
    @Override
    @Cacheable(key = "'user:' + #p0")
    public List<MenuDto> findByUserId(Long currentUserId) {
        List<RoleSmallDto> roles = usersRolesService.findRoleByUsersId(currentUserId);
        Set<Long> roleIds = roles.stream().map(RoleSmallDto::getId).collect(Collectors.toSet());
        List<Menu> list = menuMapper.selectListByRoles(roleIds);
        //去重
        List<Menu> menuList = list.stream().distinct().collect(Collectors.toList());
        return generator.convert(menuList, MenuDto.class);
    }

    /**
     * 公共方法提取出来
     *
     * @param resources
     */
    private void isExitHttp(Menu resources) {
        if (resources.getIFrame()) {
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
    }
}
