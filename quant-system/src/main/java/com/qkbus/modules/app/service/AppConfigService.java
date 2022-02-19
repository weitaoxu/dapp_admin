package com.qkbus.modules.app.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.app.domain.AppConfig;
import com.qkbus.modules.app.service.dto.AppConfigDto;
import com.qkbus.modules.app.service.dto.AppConfigQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2021-06-10
 */
public interface AppConfigService extends BaseService<AppConfig> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map
     * <String,Object>
     */
    Map<String, Object> queryAll(AppConfigQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List
     * <ConfigDto>
     */
    List<AppConfig> queryAll(AppConfigQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List
                          <AppConfigDto> all, HttpServletResponse response) throws IOException;

    /**
     * 创建
     *
     * @param resources /
     * @return ConfigDto
     */
    void create(AppConfig resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(AppConfig resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Set<Long> ids);

}
