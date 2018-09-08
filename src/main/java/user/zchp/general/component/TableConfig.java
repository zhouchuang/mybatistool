package user.zchp.general.component;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表信息
 *
 * @author zhouchuang
 * @create 2018-08-22 22:16
 */
public class TableConfig {
    List<Column> defaultColumnList = new ArrayList<Column>();
    String basePath ;
    public static TableConfig me(){
        return new TableConfig();
    }
    public TableConfig addDefaultColumn(Column column){
        this.defaultColumnList.add(column);
        return this;
    }
    public TableConfig setDefaultPath(String path){
        this.basePath = path;
        return this;
    }
}
