package ${package}.domain;
import com.baomidou.mybatisplus.annotation.*;
import com.qkbus.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
<#if isNotNullColumns??>
    import javax.validation.constraints.*;
</#if>
<#if hasDateAnnotation>
</#if>
<#if hasTimestamp>
    import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
    import java.math.BigDecimal;
</#if>
import java.io.Serializable;

/**
* @author ${author}
* @date ${date}
*/
@Data
@TableName("${tableName}")
public class ${className}  extends BaseEntity  implements Serializable {
<#if columns??>
    <#list columns as column>
        <#if column.columnKey = 'PRI'>
            @TableId(value = "id", type = IdType.AUTO)
        </#if>
        <#if column.remark != ''>
            @ApiModelProperty(value ="${column.remark}")
        </#if>
        <#if column.istNotNull && column.columnKey != 'PRI'>
            <#if column.columnType = 'String'>
                @NotBlank
            <#else>
                @NotNull
            </#if>
        </#if>
        <#if column.dateAnnotation?? &&column.dateAnnotation!="">
            <#if column.dateAnnotation = 'CreationTimestamp'>
                @TableField(fill= FieldFill.INSERT)
            </#if>
            <#if column.dateAnnotation = 'UpdateTimestamp'>
                @TableField(fill= FieldFill.INSERT_UPDATE)
            </#if>
        </#if>
        private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>

public void copy(${className} source){
BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
}
}
