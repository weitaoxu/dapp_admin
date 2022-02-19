
package com.qkbus.modules.system.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.system.domain.DictDetail;
import com.qkbus.modules.system.service.dto.DictDetailDto;
import com.qkbus.modules.system.service.dto.DictDetailQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
public interface DictDetailService extends BaseService<DictDetail> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(DictDetailQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<DictDetailDto>
     */
    List<DictDetail> queryAll(DictDetailQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DictDetailDto> all, HttpServletResponse response) throws IOException;


    /**
     * 创建
     *
     * @param resources /
     * @return /
     */
    void create(DictDetail resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(DictDetail resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void delete(Long id);
}
