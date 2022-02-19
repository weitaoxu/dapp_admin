
package com.qkbus.modules.app.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.app.domain.AppVersion;
import com.qkbus.modules.app.service.dto.AppVersionDto;
import com.qkbus.modules.app.service.dto.AppVersionQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2021-05-13
 */
public interface AppVersionService extends BaseService<AppVersion> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map
     * <String,Object>
     */
    Map<String, Object> queryAll(AppVersionQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List
     * <AppVersionDto>
     */
    List<AppVersion> queryAll(AppVersionQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<AppVersionDto> all, HttpServletResponse response) throws IOException;

    /**
     * 创建
     *
     * @param resources /
     * @return AppVersionDto
     */
    void create(AppVersion resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(AppVersion resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Set<Long> ids);

}
