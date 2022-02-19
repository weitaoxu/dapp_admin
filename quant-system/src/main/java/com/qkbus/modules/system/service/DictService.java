
package com.qkbus.modules.system.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.system.domain.Dict;
import com.qkbus.modules.system.service.dto.DictDto;
import com.qkbus.modules.system.service.dto.DictQueryCriteria;
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
public interface DictService extends BaseService<Dict> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(DictQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<DictDto>
     */
    List<Dict> queryAll(DictQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DictDto> all, HttpServletResponse response) throws IOException;

    /**
     * 创建
     *
     * @param resources /
     * @return /
     */
    void create(Dict resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Dict resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void delete(Set<Long> ids);

}
