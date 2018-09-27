package user.zchp.general.resource;

import user.zchp.general.component.ClassModel;
import user.zchp.general.component.Column;
import user.zchp.general.process.TableProcess;
import user.zchp.general.utils.AbstractDataResource;
import user.zchp.general.utils.SpringResouceUtil;
import user.zchp.general.utils.StringUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
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
        Connection conn = null;
        try{
            conn = getConn();
            Statement colmunment = conn.createStatement();
            ResultSet result = colmunment.executeQuery("select COLUMN_NAME,column_comment from INFORMATION_SCHEMA.Columns where table_name='"+tableProcess.getCurentTable().getTableName()+"' and table_schema='"+ SpringResouceUtil.getInstance().getDatabase()+"'");
            while(result.next()){
                comments.put(result.getString("COLUMN_NAME"), result.getString("column_comment"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            releaseConn(conn);
        }
        return comments;
    }

    public ClassModel getClassModel(TableProcess tableProcess){
        Map<String,String> comments  = getComments(tableProcess);
        ClassModel cm  = new ClassModel();
        Connection conn = null;
        try{
            conn = getConn();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(conn.getCatalog(), SpringResouceUtil.getInstance().getDatabase(), tableProcess.getCurentTable().getTableName(), null);
            cm.setName(StringUtils.getFirstCharToLower(tableProcess.getCurentTable().getName()));
            cm.setPackageName(tableProcess.getConfig().getBasePackage());
            cm.setTableName(tableProcess.getCurentTable().getTableName());
//            cm.setPath(tableProcess.getConfig().getBasePath());
            while(rs.next()) {
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
                column.setIsBase((tableProcess.getConfig().isBaseColumn(column.getName()))?Boolean.TRUE:Boolean.FALSE);
                cm.addColumn(column);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            releaseConn(conn);
        }
        return cm;
    }

    //新增基本列
    public MysqlDataResource addBaseColumn(TableProcess tableProcess){
        Connection conn = null;
        try {
            conn = getConn();
            Statement  statement = conn.createStatement();
            List<Column> columnList = tableProcess.getConfig().getDefaultColumnList();
            for(Column column : columnList){
                try{
                    statement.executeUpdate("alter table "+tableProcess.getCurentTable().getTableName()+" add ("+column.getName()+" "+column.getDataType().dbtype+"("+column.getDataType().len+")) ");
                }catch(SQLException e){
//                    System.out.println(tableProcess.getCurentTable().getTableName()+"已经存在'"+column.getName()+"'列\n"+e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            releaseConn(conn);
        }
        return this;
    }
}
