package user.zchp.general.utils;

import user.zchp.general.resource.DataResource;

import java.sql.*;

/**
 * 抽象资源类
 *
 * @author:Administrator
 * @create 2018-09-08 18:14
 */
public abstract class AbstractDataResource implements DataResource {
    public AbstractDataResource(){

    }
    @Override
    public Connection getConn()throws Exception{
        return SpringResouceUtil.getInstance().getJdbcUtil().getConn();
    }

    @Override
    public void releaseConn(Connection conn){
        SpringResouceUtil.getInstance().getJdbcUtil().releaseConn(conn);
    }

    public Connection reConnect(Connection connection){
        return SpringResouceUtil.getInstance().getJdbcUtil().reConnect(connection);
    }


    public Statement getStatement(Connection connection){
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            connection  = reConnect(connection);
            return getStatement(connection);
        }
    }

    public DatabaseMetaData getDatabaseMetaData(Connection connection){
        try {
            return connection.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
            connection  = reConnect(connection);
            return getDatabaseMetaData(connection);
        }
    }

    public ResultSet getResultSetByQuery(Connection connection,String sql){
        try {
            return  getStatement(connection).executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            connection  = reConnect(connection);
            return getResultSetByQuery(connection,sql);
        }
    }

    public void getResultSetByUpdate(Connection connection,String sql){
        try {
            getStatement(connection).executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            if(!e.getMessage().startsWith("Duplicate")){
                connection  = reConnect(connection);
                getResultSetByQuery(connection,sql);
            }
        }
    }
    public ResultSet getResultSetByMetaData(Connection connection, String schemaPattern,
                                            String tableNamePattern, String columnNamePattern){
        try {
            return  getDatabaseMetaData(connection).getColumns(connection.getCatalog(),schemaPattern,tableNamePattern,columnNamePattern);
        } catch (SQLException e) {
            e.printStackTrace();
            connection  = reConnect(connection);
            return getResultSetByMetaData(connection,schemaPattern,tableNamePattern,columnNamePattern);
        }
    }
}
