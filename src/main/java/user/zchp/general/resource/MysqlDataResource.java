package user.zchp.general.resource;

import user.zchp.general.component.ClassModel;
import user.zchp.general.component.Column;
import user.zchp.general.process.TableProcess;
import user.zchp.general.utils.AbstractDataResource;
import user.zchp.general.utils.SpringResouceUtil;
import user.zchp.general.utils.StringUtils;
import user.zchp.utils.PageParam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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


    private Map getComments(String tableName){
        Map<String,String> comments = new HashMap<String,String>();
        Connection conn = null;
        try{
            conn = getConn();
           /* Statement colmunment = conn.createStatement();
            ResultSet result = colmunment.executeQuery("select COLUMN_NAME,column_comment from INFORMATION_SCHEMA.Columns where table_name='"+tableName+"' and table_schema='"+ SpringResouceUtil.getInstance().getDatabase()+"'");
            */
            ResultSet result = getResultSetByQuery(conn,"select COLUMN_NAME,column_comment from INFORMATION_SCHEMA.Columns where table_name='"+tableName+"' and table_schema='"+ SpringResouceUtil.getInstance().getDatabase()+"'");
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
        Map<String,String> comments  = getComments(tableProcess.getCurentTable().getTableName());
        ClassModel cm  = new ClassModel();
        Connection conn = null;
        try{
            conn = getConn();
            /*DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(conn.getCatalog(), SpringResouceUtil.getInstance().getDatabase(), tableProcess.getCurentTable().getTableName(), null);
            */
            ResultSet rs = getResultSetByMetaData(conn,SpringResouceUtil.getInstance().getDatabase(), tableProcess.getCurentTable().getTableName(), null);
            cm.setName(StringUtils.getFirstCharToLower(tableProcess.getCurentTable().getName()));
            cm.setPackageName(tableProcess.getConfig().getBasePackage());
            cm.setTableName(tableProcess.getCurentTable().getTableName());
            cm.setPkId(tableProcess.getConfig().getPk().getName());
//            cm.setPath(tableProcess.getConfig().getBasePath());
            while(rs.next()) {
                if(!cm.getPkId().equalsIgnoreCase(rs.getString("COLUMN_NAME"))){
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
                getResultSetByUpdate(conn,"alter table "+tableProcess.getCurentTable().getTableName()+" add ("+column.getName()+" "+column.getDataType().dbtype+"("+column.getDataType().len+")) ");
                //statement.executeUpdate("alter table "+tableProcess.getCurentTable().getTableName()+" add ("+column.getName()+" "+column.getDataType().dbtype+"("+column.getDataType().len+")) ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            releaseConn(conn);
        }
        return this;
    }

    //获取所有的表名称
    public List<String>  tableList(PageParam queryParam){
        List<String> list = new ArrayList<String>();
        Connection  conn = null;
        try {
            conn = getConn();
            /*Statement colmunment = conn.createStatement();
            ResultSet result = colmunment.executeQuery("select table_name from information_schema.TABLES where TABLE_SCHEMA= \'"+queryParam.getCondition().get("database")+"\' order by table_name ");
            */
            String wheresql = " and 1=1 ";
            if(StringUtils.isNotEmptyString(queryParam.getCondition().get("key"))){
                wheresql += " and table_name like '" +queryParam.getCondition().get("key").toString()+"%'";
            }
            ResultSet result = getResultSetByQuery(conn,"select table_name from information_schema.TABLES where TABLE_SCHEMA= \'"+queryParam.getCondition().get("database")+"\'"+wheresql+" order by table_name limit "+(queryParam.getCurrentPage()-1)*queryParam.getPageSize()+","+queryParam.getCurrentPage()*queryParam.getPageSize());
            while(result.next()){
                String tableName = result.getString("table_name");
                list.add(tableName);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConn(conn);
        }
        return list;
    }


    //获取所有的列信息
    public List<Column> fieldList(String tableName){

        List<Column> list = new ArrayList<Column>();
        Map<String,String> comments  = getComments(tableName);
        Connection conn = null;
        try {
            conn = getConn();
           /* DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(conn.getCatalog(), SpringResouceUtil.getInstance().getDatabase(), tableName, null);
            */
            ResultSet rs = getResultSetByMetaData(conn,SpringResouceUtil.getInstance().getDatabase(), tableName, null);
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
                list.add(column);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            releaseConn(conn);
        }
        return list;
    }

}
