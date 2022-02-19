
package com.qkbus.tools.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.tools.domain.QiniuContent;
import com.qkbus.tools.service.dto.QiniuContentDto;
import com.qkbus.tools.service.dto.QiniuQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
public interface QiniuContentService extends BaseService<QiniuContent> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(QiniuQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<QiniuContentDto>
     */
    List<QiniuContent> queryAll(QiniuQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<QiniuContentDto> all, HttpServletResponse response) throws IOException;
}
