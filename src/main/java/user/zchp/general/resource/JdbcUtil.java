package user.zchp.general.resource;

import user.zchp.service.BusinessTableService;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

/**
 * JDBC工具类
 *
 * @author:Administrator
 * @create 2018-09-26 16:23
 */
public class JdbcUtil {

    private  String driver;
    private  String url ;
    public  String username;
    private  String password;
    private  LinkedList<Connection> connectPoll;
    private  int initCount = 1;//初始化连接数
    private  int maxCount = 5;//最大连接数
    private  int currentCount = 1;//当前连接数

    private static class JdbcUtilHolder {
        private static final JdbcUtil INSTANCE = new JdbcUtil();
    }

    private JdbcUtil (){
        try {
            Properties properties=new Properties();
            //获得输入流
            InputStream is=BusinessTableService.class.getClassLoader().getResourceAsStream("jdbc.properties");
            System.out.println("我正在读取文件");
            properties.load(is);
            System.out.println("成功读取");
            is.close();
            driver = (String)properties.get("business.jdbc.driver");
            url = (String)properties.get("business.jdbc.url");
            username = (String)properties.get("business.jdbc.username");
            password = (String)properties.get("business.jdbc.password");

            Class.forName(driver);
            connectPoll = new LinkedList<Connection>();
            try {
                for(int i=0; i<=initCount; i++){//初始化生成5个数据库连接
                    connectPoll.addLast(createConnection());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final JdbcUtil getInstance() {
        return JdbcUtilHolder.INSTANCE;
    }



    private   Connection createConnection() throws SQLException{
        return DriverManager.getConnection(url, username, password);
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
