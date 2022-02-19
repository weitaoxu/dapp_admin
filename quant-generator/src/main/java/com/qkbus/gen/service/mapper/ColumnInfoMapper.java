
package com.qkbus.gen.service.mapper;

import com.qkbus.common.mapper.CoreMapper;
import com.qkbus.gen.domain.ColumnConfig;
import com.qkbus.gen.domain.vo.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ColumnInfoMapper extends CoreMapper<ColumnConfig> {

    @Select("<script>select table_name ,create_time , engine, table_collation as coding, table_comment as remark from information_schema.tables " +
            "where table_schema = (select database()) <if test=\"name!=null\"> and table_name like CONCAT('%',#{name},'%') </if> order by create_time desc</script>")
    List<TableInfo> selectTablePage(@Param("name") String name);

    @Select("<script>select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
            "where table_schema = (select database()) order by create_time desc</script>")
    List<TableInfo> selectTables();

    @Select("SELECT COLUMN_NAME, IS_NULLABLE, DATA_TYPE, COLUMN_COMMENT, COLUMN_KEY, EXTRA FROM INFORMATION_SCHEMA.COLUMNS " +
            "WHERE TABLE_NAME = #{name} AND TABLE_SCHEMA = (SELECT DATABASE()) ORDER BY ORDINAL_POSITION")
    List<Map<String, Object>> queryByTableName(@Param("name") String name);

}
