
package com.qkbus.tools.service.impl;

import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.tools.domain.QiniuConfig;
import com.qkbus.tools.service.QiniuConfigService;
import com.qkbus.tools.service.dto.QiniuConfigDto;
import com.qkbus.tools.service.dto.QiniuQueryCriteria;
import com.qkbus.tools.service.mapper.QiniuConfigMapper;
import com.qkbus.utils.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QiniuConfigServiceImpl extends BaseServiceImpl<QiniuConfigMapper, QiniuConfig> implements QiniuConfigService {

    private final IGenerator generator;


    @Override
    public Map<String, Object> queryAll(QiniuQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<QiniuConfig> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), QiniuConfigDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<QiniuConfig> queryAll(QiniuQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(QiniuConfig.class, criteria));
    }


    @Override
    public void download(List<QiniuConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QiniuConfigDto qiniuConfig : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("accessKey", qiniuConfig.getAccessKey());
            map.put("Bucket 识别符", qiniuConfig.getBucket());
            map.put("外链域名", qiniuConfig.getHost());
            map.put("secretKey", qiniuConfig.getSecretKey());
            map.put("空间类型", qiniuConfig.getType());
            map.put("机房", qiniuConfig.getZone());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
