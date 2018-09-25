package user.zchp.general.component;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表信息
 *
 * @author zhouchuang
 * @create 2018-08-22 22:16
 */
@Data
public class TableConfig {
    Column pk;
    Column version;
    List<Column> defaultColumnList = new ArrayList<Column>();
    String basePackage ;
    String database ;
    String basePath;
    public static TableConfig me(){
        return new TableConfig();
    }
    public TableConfig addDefaultColumn(Column column){
        this.defaultColumnList.add(column);
        return this;
    }
    public TableConfig setBasePackage(String basePackage){
        this.basePackage = basePackage;
        return this;
    }
    public TableConfig setDatabase(String database){
        this.database = database;
        return this;
    }
    public TableConfig setBasePath(String basePath){
        this.basePath  = basePath;
        return this;
    }
    public  Boolean isBaseColumn(String name){
        for(Column column : this.defaultColumnList){
            if(column.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public TableConfig setPk(Column column){
        this.pk = column;
        this.defaultColumnList.add(column);
        return this;
    }

    public TableConfig setVersion(Column column){
        this.version = column;
        this.defaultColumnList.add(column);
        return this;
    }

}
