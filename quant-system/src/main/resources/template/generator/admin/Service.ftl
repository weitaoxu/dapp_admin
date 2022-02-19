package ${package}.service;
import com.qkbus.common.service.BaseService;
import ${package}.domain.${className};
import ${package}.service.dto.${className}Dto;
import ${package}.service.dto.${className}QueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

/**
* @author ${author}
* @date ${date}
*/
public interface ${className}Service extends BaseService<${className}>{

/**
* 查询数据分页
* @param criteria 条件
* @param pageable 分页参数
* @return Map
<String,Object>
*/
Map
<String,Object> queryAll(${className}QueryCriteria criteria, Pageable pageable);

/**
* 查询所有数据不分页
* @param criteria 条件参数
* @return List
<${className}Dto>
    */
    List<${className}> queryAll(${className}QueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List
    <${className}Dto> all, HttpServletResponse response) throws IOException;

        /**
        * 创建
        * @param resources /
        * @return ${className}Dto
        */
        void create(${className} resources);

        /**
        * 编辑
        * @param resources /
        */
        void update(${className} resources);

        /**
        * 多选删除
        * @param ids /
        */
        void deleteAll( Set<${pkColumnType}> ids);

        }
