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
    public String driver;
    public String url ;
    public String username;
    public String password;
    public Connection conn = null;
    private static int initCount = 5;//初始化连接数
    private static int maxCount = 10;//最大连接数
    private static int currentCount = 5;//当前连接数
    public AbstractDataResource(){
        init();
    }

    private LinkedList<Connection> connectPoll;

    @PostConstruct
    public void init(){
        try{
            Properties properties=new Properties();
            //获得输入流
            InputStream is=BusinessTableService.class.getClassLoader().getResourceAsStream("jdbc.properties");
            System.out.println("我正在读取文件");
            properties.load(is);
            System.out.println("我成功读取");
            is.close();
            driver = (String)properties.get("business.jdbc.driver");
            url = (String)properties.get("business.jdbc.url");
            username = (String)properties.get("business.jdbc.username");
            password = (String)properties.get("business.jdbc.password");

            initConnnectPoll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void initConnnectPoll()throws ClassNotFoundException{
        //加载驱动
        Class.forName(driver);
        //初始化5个
        connectPoll = new LinkedList<Connection>();
        try {
            for(int i=0; i<=initCount; i++){//初始化生成5个数据库连接
                connectPoll.addLast(this.createConnection());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() throws SQLException{
        return DriverManager.getConnection(url, username, password);
    }



    @Override
    public Connection getConn()throws Exception{
        /*Class.forName(driver);// 动态加载mysql驱动
        conn =   DriverManager.getConnection(url,username,password);
        return conn;*/

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

    @Override
    public void releaseConn(Connection conn){
        if(conn!=null){
            connectPoll.addLast(conn);
        }
    }

    public void releaseConn(){
        if(this.conn!=null){
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
