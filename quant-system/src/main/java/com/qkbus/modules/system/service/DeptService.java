
package com.qkbus.modules.system.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.system.domain.Dept;
import com.qkbus.modules.system.service.dto.DeptDto;
import com.qkbus.modules.system.service.dto.DeptQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
public interface DeptService extends BaseService<Dept> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(DeptQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<DeptDto>
     */
    List<Dept> queryAll(DeptQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DeptDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据PID查询
     *
     * @param pid /
     * @return /
     */
    List<Dept> findByPid(long pid);

    /**
     * 构建树形数据
     *
     * @param deptDtos 原始数据
     * @return /
     */
    Object buildTree(List<DeptDto> deptDtos);


    /**
     * 删除部门
     *
     * @param deptIds
     */
    void delDepts(Set<Long> deptIds);


    /**
     * 修改部门
     */
    void updateDept(Dept resources);

    /**
     * 创建部门
     */
    void createDept(Dept resources);

    /**
     * 获取待删除的部门
     * @param deptList /
     * @param deptDtos /
     * @return /
     */
    /*Set<DeptDto> getDeleteDepts(List<Dept> deptList, Set<DeptDto> deptDtos);*/

    /**
     * 根据角色ID查询
     *
     * @param id /
     * @return /
     */
    Set<Dept> findByRoleIds(Long id);


    List<Long> getDeptChildren(List<Dept> deptList);

    Set<Long> getDeptIds();
}