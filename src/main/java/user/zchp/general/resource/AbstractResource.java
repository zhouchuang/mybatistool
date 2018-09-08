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
public abstract class AbstractResource implements Resource {
    private String driver;
    private String url ;
    private String username;
    private String password;

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
        System.out.println("成功加载MySQL驱动程序");
        return  DriverManager.getConnection(url,username,password);
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
}
