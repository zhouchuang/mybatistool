package user.zchp.general.resource;

import user.zchp.service.BusinessTableService;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 抽象资源类
 *
 * @author:Administrator
 * @create 2018-09-08 18:14
 */
public abstract class AbstractDataResource implements DataResource {
    protected String driver;
    protected String url ;
    protected String username;
    protected String password;
    protected Connection conn = null;

    public AbstractDataResource(){
        init();
    }

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConn()throws Exception{
        Class.forName(driver);// 动态加载mysql驱动
        conn =   DriverManager.getConnection(url,username,password);
        return conn;
    }

    @Override
    public void releaseConn(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
