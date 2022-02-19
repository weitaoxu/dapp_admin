
package com.qkbus.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.modules.system.domain.Dept;
import com.qkbus.modules.system.domain.Job;
import com.qkbus.modules.system.domain.RolesDepts;
import com.qkbus.modules.system.service.DeptService;
import com.qkbus.modules.system.service.UserService;
import com.qkbus.modules.system.service.UsersRolesService;
import com.qkbus.modules.system.service.dto.DeptDto;
import com.qkbus.modules.system.service.dto.DeptQueryCriteria;
import com.qkbus.modules.system.service.dto.RoleSmallDto;
import com.qkbus.modules.system.service.dto.UserDto;
import com.qkbus.modules.system.service.mapper.DeptMapper;
import com.qkbus.modules.system.service.mapper.JobMapper;
import com.qkbus.modules.system.service.mapper.RolesDeptsMapper;
import com.qkbus.utils.FileUtil;
import com.qkbus.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
public class DeptServiceImpl extends BaseServiceImpl<DeptMapper, Dept> implements DeptService {

    private final IGenerator generator;

    private final DeptMapper deptMapper;

    private final JobMapper jobMapper;

    private final RolesDeptsMapper rolesDeptsMapper;
    private final UserService userService;

    private final UsersRolesService usersRolesService;

    private final String[] scopeType = {"全部", "本级", "自定义"};

    @Override
    public Map<String, Object> queryAll(DeptQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Dept> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), DeptDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<Dept> queryAll(DeptQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(Dept.class, criteria));
    }


    @Override
    public void download(List<DeptDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeptDto dept : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", dept.getName());
            map.put("上级部门", dept.getPid());
            map.put("状态", dept.getEnabled());
            map.put("创建日期", dept.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 根据PID查询
     *
     * @param pid /
     * @return /
     */
    @Override
    public List<Dept> findByPid(long pid) {
        DeptQueryCriteria criteria = new DeptQueryCriteria();
        criteria.setPid(pid);
        return baseMapper.selectList(QueryHelpPlus.getPredicate(Dept.class, criteria));
    }

    /**
     * 构建树形数据
     *
     * @param deptDtos 原始数据
     * @return /
     */
    @Override
    public Object buildTree(List<DeptDto> deptDtos) {
        Set<DeptDto> trees = new LinkedHashSet<>();
        Set<DeptDto> depts = new LinkedHashSet<>();
        List<String> deptNames = deptDtos.stream().map(DeptDto::getName).collect(Collectors.toList());
        boolean isChild;
        DeptQueryCriteria criteria = new DeptQueryCriteria();
        List<Dept> deptList = this.queryAll(criteria);
        for (DeptDto deptDto : deptDtos) {
            isChild = false;
            if ("0".equals(deptDto.getPid().toString())) {
                trees.add(deptDto);
            }
            for (DeptDto it : deptDtos) {
                if (it.getPid().equals(deptDto.getId())) {
                    isChild = true;
                    if (deptDto.getChildren() == null) {
                        deptDto.setChildren(new ArrayList<>());
                    }
                    deptDto.getChildren().add(it);
                }
            }
            if (isChild) {
                depts.add(deptDto);
                for (Dept dept : deptList) {
                    if (dept.getId().equals(deptDto.getPid()) && !deptNames.contains(dept.getName())) {
                        depts.add(deptDto);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }
        Integer totalElements = deptDtos.size();
        Map<String, Object> map = new HashMap<>(2);
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? deptDtos : trees);
        return map;
    }

    /**
     * 删除部门
     *
     * @param deptIds
     */
    @Override
    public void delDepts(Set<Long> deptIds) {
        for (Long id : deptIds) {
            List<Dept> deptList = this.findByPid(id);
            if (CollectionUtil.isNotEmpty(deptList)) {
                for (Dept d : deptList) {
                    deptIds.add(d.getId());
                }
            }
        }
        int jobCount = jobMapper.selectCount(Wrappers.<Job>lambdaQuery().in(Job::getDeptId, deptIds));
        int roleCount = rolesDeptsMapper.selectCount(Wrappers.<RolesDepts>lambdaQuery()
                .in(RolesDepts::getDeptId, deptIds));
        if (jobCount > 0) {
            throw new BadRequestException("所选部门中存在与岗位关联，请取消关联后再试");
        }
        if (roleCount > 0) {
            throw new BadRequestException("所选部门中存在与角色关联，请取消关联后再试");
        }
        this.removeByIds(deptIds);
    }

    @Override
    public void updateDept(Dept resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        this.updateById(resources);
    }

    @Override
    public void createDept(Dept resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new dept cannot already have an ID");
        }
        this.save(resources);
    }


    /**
     * 根据角色ID查询
     *
     * @param id /
     * @return /
     */
    @Override
    public Set<Dept> findByRoleIds(Long id) {
        return deptMapper.findDeptByRoleId(id);
    }


    @Override
    public Set<Long> getDeptIds() {
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        // 用于存储部门id
        Set<Long> deptIds = new HashSet<>();
        // 查询用户角色
        List<RoleSmallDto> roles = usersRolesService.findRoleByUsersId(user.getId());
        for (RoleSmallDto role : roles) {
            if (scopeType[0].equals(role.getDataScope())) {
                return new HashSet<>();
            }
            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
                deptIds.add(user.getDept().getId());
            }
            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                Set<Dept> depts = this.findByRoleIds(role.getId());
                for (Dept dept : depts) {
                    deptIds.add(dept.getId());
                    List<Dept> deptChildren = this.findByPid(dept.getId());
                    if (deptChildren != null && deptChildren.size() != 0) {
                        deptIds.addAll(getDeptChildren(deptChildren));
                    }
                }
            }
        }
        return deptIds;
    }


    @Override
    public List<Long> getDeptChildren(List<Dept> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept != null && dept.getEnabled()) {
                        List<Dept> depts = this.findByPid(dept.getId());
                        if (depts.size() != 0) {
                            list.addAll(getDeptChildren(depts));
                        }
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }
}
