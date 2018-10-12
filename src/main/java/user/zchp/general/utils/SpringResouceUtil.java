package user.zchp.general.utils;

import lombok.Data;
import org.springframework.stereotype.Component;
import user.zchp.utils.SpringContextUtil;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

/**
 * 资源读取工具
 *
 * @author:Administrator
 * @create 2018-09-27 15:00
 */
@Component
@Data
public class SpringResouceUtil {
    private String username;
    private String password;
    private String driver;
    private String database;
    private String url;
    private JdbcUtil jdbcUtil;
//    private GeneralMessage message;
    private Map<String,Object> store = new HashMap<String,Object>();


    @PostConstruct
    public void init(){

        try{
            Properties properties=new Properties();
            InputStream is= SpringResouceUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            System.out.println("读取代码生成器资源文件");
            properties.load(is);
            is.close();
            driver = (String)properties.get("business.jdbc.driver");
            url = (String)properties.get("business.jdbc.url");
            username = (String)properties.get("business.jdbc.username");
            password = (String)properties.get("business.jdbc.password");
            database = (String)properties.get("business.jdbc.database");
            jdbcUtil = new JdbcUtil();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class JdbcUtil{
        private LinkedList<Connection> connectPoll;
        private int initCount = 2;//初始化连接数
        private int maxCount = 5;//最大连接数
        private int currentCount = 2;//当前连接数

        public JdbcUtil(){
            try{
                System.out.println("初始化数据库连接池");
                Class.forName(driver);
                connectPoll = new LinkedList<Connection>();
                try {
                    for(int i=0; i<initCount; i++){//初始化生成5个数据库连接
                        connectPoll.addLast(createConnection());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        private   Connection createConnection() throws SQLException{
            return DriverManager.getConnection(url, username, password);
        }

        void reConnect(Connection connection){
            System.out.println("重新连接");
            if(connection!=null){
                connectPoll.remove(connection);
                currentCount--;
            }
            try {
                connectPoll.addLast(createConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    private static SpringResouceUtil instance;
    public static SpringResouceUtil getInstance(){
        if(instance==null){
            instance = SpringContextUtil.getBean("springResouceUtil");
        }
        return instance;
    }
}
