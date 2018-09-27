package user.zchp.general.utils;

import user.zchp.general.resource.DataResource;
import user.zchp.general.utils.JdbcUtil;

import java.sql.Connection;

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
        return SpringResource.getJdbcUtil().getConn();
    }

    @Override
    public void releaseConn(Connection conn){
        SpringResource.getJdbcUtil().releaseConn(conn);
    }
}
