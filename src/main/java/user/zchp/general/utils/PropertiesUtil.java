package user.zchp.general.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Properties;

/**
 * 资源读取工具
 *
 * @author:Administrator
 * @create 2018-09-27 15:00
 */
@Component
@Data
public class PropertiesUtil {
    private String username;
    private String password;
    private String driver;
    private String database;
    private String url;

    @PostConstruct
    public void init(){

        try{
            Properties properties=new Properties();
            InputStream is=JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            System.out.println("正在读取代码生成器资源文件");
            properties.load(is);
            is.close();
            driver = (String)properties.get("business.jdbc.driver");
            url = (String)properties.get("business.jdbc.url");
            username = (String)properties.get("business.jdbc.username");
            password = (String)properties.get("business.jdbc.password");
            database = (String)properties.get("business.jdbc.database");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
