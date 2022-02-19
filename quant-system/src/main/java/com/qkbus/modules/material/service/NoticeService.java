
package com.qkbus.modules.material.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.material.domain.Notice;
import com.qkbus.modules.material.service.dto.NoticeDto;
import com.qkbus.modules.material.service.dto.NoticeQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2021-05-14
 */
public interface NoticeService extends BaseService<Notice> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map
     * <String,Object>
     */
    Map<String, Object> queryAll(NoticeQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List
     * <NoticeDto>
     */
    List<Notice> queryAll(NoticeQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<NoticeDto> all, HttpServletResponse response) throws IOException;

    /**
     * 创建
     *
     * @param resources /
     * @return NoticeDto
     */
    void create(Notice resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Notice resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Set<Long> ids);

}
