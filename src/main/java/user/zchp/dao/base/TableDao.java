package user.zchp.dao.base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 表dao层
 *
 * @author zhouchuang
 * @create 2018-05-12 14:43
 */
public interface TableDao extends Dao {

    @Select("select table_name from information_schema.TABLES where TABLE_SCHEMA= #{database} order by table_name ")
    List<String> tableList(@Param("database") String database);



    @Select("show full fields from ${table}")
    List<Map> fieldList(@Param("table") String table);
}
