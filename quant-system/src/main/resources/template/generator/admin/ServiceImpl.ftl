package ${package}.service.impl;

import ${package}.domain.${className};
<#if columns??>
    <#list columns as column>
        <#if column.columnKey = 'UNI'>
            <#if column_index = 1>
                import com.qkbus.exception.EntityExistException;
            </#if>
        </#if>
    </#list>
</#if>
import lombok.AllArgsConstructor;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.utils.FileUtil;
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}Dto;
import ${package}.service.dto.${className}QueryCriteria;
import ${package}.service.mapper.${className}Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
<#if !auto && pkColumnType = 'Long'>
    import cn.hutool.core.lang.Snowflake;
    import cn.hutool.core.util.IdUtil;
</#if>
<#if !auto && pkColumnType = 'String'>
    import cn.hutool.core.util.IdUtil;
</#if>
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author ${author}
* @date ${date}
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ${className}ServiceImpl extends BaseServiceImpl
<${className}Mapper, ${className}> implements ${className}Service {

private final IGenerator generator;

@Override
public Map
<String, Object> queryAll(${className}QueryCriteria criteria, Pageable pageable) {
getPage(pageable);
PageInfo<${className}> page = new PageInfo<>(queryAll(criteria));
Map
<String, Object> map = new LinkedHashMap<>(2);
map.put("content", generator.convert(page.getList(), ${className}Dto.class));
map.put("totalElements", page.getTotal());
return map;
}


@Override
public List<${className}> queryAll(${className}QueryCriteria criteria){
return this.list(QueryHelpPlus.getPredicate(${className}.class, criteria));
}


@Override
public void download(List
<${className}Dto> all, HttpServletResponse response) throws IOException {
    List
    <Map
    <String , Object>> list = new ArrayList<>();
        for (${className}Dto ${changeClassName} : all) {
        Map
        <String ,Object> map = new LinkedHashMap<>();
            <#list columns as column>
                <#if column.columnKey != 'PRI'>
                    <#if column.remark != ''>
                        map.put("${column.remark}", ${changeClassName}.get${column.capitalColumnName}());
                    <#else>
                        map.put(" ${column.changeColumnName}",  ${changeClassName}.get${column.capitalColumnName}());
                    </#if>
                </#if>
            </#list>
            list.add(map);
            }
            FileUtil.downloadExcel(list, response);
            }

            @Override
            @Transactional(rollbackFor = Exception.class)
            public void create(${className} resources) {
            this.save(resources);
            }

            @Override
            @Transactional(rollbackFor = Exception.class)
            public void update(${className} resources) {
            this.updateById(resources);
            }

            @Override
            public void deleteAll(Set<${pkColumnType}> ids) {
            this.removeByIds(ids);
            }
            }
