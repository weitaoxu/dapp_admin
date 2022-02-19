package ${package}.service.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
<#if queryHasTimestamp>
    import java.sql.Timestamp;
</#if>
<#if queryHasBigDecimal>
    import java.math.BigDecimal;
</#if>
<#if betweens??>
    import java.util.List;
</#if>
<#if queryColumns??>
    import com.qkbus.annotation.Query;
</#if>

/**
* @author ${author}
* @date ${date}
*/
@Data
public class ${className}QueryCriteria{
<#if queryColumns??>
    <#list queryColumns as column>
        <#if column.queryType = '='>
            @Query
            <#if column.remark != ''>
                @ApiModelProperty(value ="${column.remark}")
            </#if>
            private ${column.columnType} ${column.changeColumnName};
        </#if>
        <#if column.queryType = 'Like'>

            @Query(type = Query.Type.INNER_LIKE)
            private ${column.columnType} ${column.changeColumnName};
        </#if>
        <#if column.queryType = '!='>

            @Query(type = Query.Type.NOT_EQUAL)
            private ${column.columnType} ${column.changeColumnName};
        </#if>
        <#if column.queryType = 'NotNull'>

            @Query(type = Query.Type.NOT_NULL)
            private ${column.columnType} ${column.changeColumnName};
        </#if>
        <#if column.queryType = '>='>

            @Query(type = Query.Type.GREATER_THAN)
            private ${column.columnType} ${column.changeColumnName};
        </#if>
        <#if column.queryType = '<='>

            @Query(type = Query.Type.LESS_THAN)
            private ${column.columnType} ${column.changeColumnName};
        </#if>
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>

        @Query(type = Query.Type.BETWEEN)
        private List<${column.columnType}> gmtCreate;
    </#list>
</#if>
}
