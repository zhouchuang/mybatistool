package user.zchp.general.utils;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * JDBC工具类
 *
 * @author:Administrator
 * @create 2018-09-26 16:23
 */
@Component
public class JdbcUtil  {

    public String iocName = "jdbcUtil";
    private  String driver;
    private  String url ;
    private  String username;
    private  String password;
    public   String database;
    private  LinkedList<Connection> connectPoll;
    private  int initCount = 1;//初始化连接数
    private  int maxCount = 5;//最大连接数
    private  int currentCount = 1;//当前连接数

    @PostConstruct
    public void  init (){
        try {
            Class.forName(SpringResource.getPropertiesUtil().getDriver());
            connectPoll = new LinkedList<Connection>();
            try {
                for(int i=0; i<=initCount; i++){//初始化生成5个数据库连接
                    connectPoll.addLast(createConnection());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("初始化数据库连接池完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private   Connection createConnection() throws SQLException{
        return DriverManager.getConnection(SpringResource.getPropertiesUtil().getUrl(), SpringResource.getPropertiesUtil().getUsername(), SpringResource.getPropertiesUtil().getPassword());
    }

    Connection getConn()throws Exception{
        synchronized (connectPoll) {//多线程并发处理
            if(connectPoll.size() > 0){
                return connectPoll.removeFirst();
            }else if(currentCount < maxCount){
                //未超过最大连接数，则新建连接
                Connection  conn = createConnection();
                connectPoll.add(conn);
                currentCount++;
                return conn;
            }else{
                throw new SQLException("连接已经用完");
            }
        }
    }

    void releaseConn(Connection conn){
        if(conn!=null){
            connectPoll.addLast(conn);
        }
    }

}
