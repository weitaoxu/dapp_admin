package ${package}.service.dto;
import com.qkbus.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
<#if hasTimestamp>
    import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
    import java.math.BigDecimal;
</#if>
import java.io.Serializable;
<#if !auto && pkColumnType == 'Long'>
    import com.fasterxml.jackson.databind.annotation.JsonSerialize;
    import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
</#if>

/**
* @author ${author}
* @date ${date}
*/
@Data
@EqualsAndHashCode(callSuper = false)
public class ${className}Dto  extends BaseDto implements Serializable {
<#if columns??>
    <#list columns as column>

        <#if column.changeColumnName != 'gmtCreate'&&column.changeColumnName != 'gmtUpdated'>
            <#if column.remark != ''>
                @ApiModelProperty("${column.remark}")
            </#if>
            <#if column.columnKey = 'PRI'>
                <#if !auto && pkColumnType = 'Long'>
                    /** 防止精度丢失 */
                    @JsonSerialize(using= ToStringSerializer.class)
                </#if>
            </#if>
            private ${column.columnType} ${column.changeColumnName};
        </#if>
    </#list>
</#if>
}
