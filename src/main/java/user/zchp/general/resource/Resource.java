package user.zchp.general.resource;

import java.sql.Connection;

/**
 * 资源
 *
 * @author:Administrator
 * @create 2018-09-08 18:07
 */
public interface Resource {
    Connection getConn()throws Exception;
    void releaseConn(Connection connection);
}
