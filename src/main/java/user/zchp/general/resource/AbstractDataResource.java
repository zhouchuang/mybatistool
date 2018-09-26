package user.zchp.general.resource;

import user.zchp.service.BusinessTableService;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

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
        return JdbcUtil.getInstance().getConn();
    }

    @Override
    public void releaseConn(Connection conn){
        JdbcUtil.getInstance().releaseConn(conn);
    }
}
