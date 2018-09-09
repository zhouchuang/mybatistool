package user.zchp.general.resource;

import user.zchp.general.component.ClassModel;
import user.zchp.general.component.Column;
import user.zchp.general.component.ColumnInfo;
import user.zchp.general.process.TableProcess;
import user.zchp.utils.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * mysql资源
 *
 * @author:Administrator
 * @create 2018-09-08 18:09
 */
public class MysqlDataResource extends AbstractDataResource {
    private static class MysqlDataResourceHolder {
        private static final MysqlDataResource INSTANCE = new MysqlDataResource();
    }

    private MysqlDataResource (){}

    public static final MysqlDataResource getInstance() {
        return MysqlDataResourceHolder.INSTANCE;
    }


    private Map getComments(TableProcess tableProcess){
        Map<String,String> comments = new HashMap<String,String>();
        try{
            Statement colmunment = getConn().createStatement();
            ResultSet result = colmunment.executeQuery("select COLUMN_NAME,column_comment from INFORMATION_SCHEMA.Columns where table_name='"+tableProcess.getLeftTable().getTable().getTableName()+"' and table_schema='"+tableProcess.getConfig().getDatabase()+"'");
            while(result.next()){
                comments.put(result.getString("COLUMN_NAME"), result.getString("column_comment"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return comments;
    }

    public ClassModel getClassModel(TableProcess tableProcess){
        Map<String,String> comments  = getComments(tableProcess);
        ClassModel cm  = new ClassModel();
        try{
            Connection conn = getConn();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(conn.getCatalog(), username, tableProcess.getLeftTable().getTable().getTableName(), null);
            cm.setName(StringUtils.getFirstCharToLower(tableProcess.getLeftTable().getTable().getName()));
            cm.setPackageName(tableProcess.getConfig().getBasePath());
            cm.setTableName(tableProcess.getLeftTable().getTable().getTableName());
            cm.setPkId("id");
            cm.setCreateBy("createBy");
            cm.setCreateTime("createTime");
            cm.setUpdateBy("updateBy");
            cm.setUpdateTime("updateTime");
            while(rs.next()) {
                if(!"id".toUpperCase().equals(rs.getString("COLUMN_NAME").toUpperCase())){
                    Column column = new Column();
                    column.setColumnSize(Integer.parseInt(rs.getString("DECIMAL_DIGITS")!=null?rs.getString("DECIMAL_DIGITS"):"0"));
                    String str = rs.getString("COLUMN_SIZE");
                    if(StringUtils.isEmpty(str)){
                        str = "0";
                    }
                    column.setDecimalNum(Integer.parseInt(str));
                    column.setColumnName(rs.getString("COLUMN_NAME"));
                    column.setColumnType(rs.getString("TYPE_NAME"));
                    column.setRemarks(comments.get(rs.getString("COLUMN_NAME")));
                    column.setDefaultValue(rs.getString("COLUMN_DEF"));
                    cm.addColumn(column);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return cm;
    }
}
