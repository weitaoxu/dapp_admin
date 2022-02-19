
package com.qkbus.modules.material.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.material.domain.Material;
import com.qkbus.modules.material.service.dto.MaterialDto;
import com.qkbus.modules.material.service.dto.MaterialQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2021-05-11
 */
public interface MaterialService extends BaseService<Material> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map
     * <String,Object>
     */
    Map<String, Object> queryAll(MaterialQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List
     * <MaterialDto>
     */
    List<Material> queryAll(MaterialQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<MaterialDto> all, HttpServletResponse response) throws IOException;

    /**
     * 创建
     *
     * @param resources /
     * @return MaterialDto
     */
    void create(Material resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Material resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Set<Integer> ids);

}
