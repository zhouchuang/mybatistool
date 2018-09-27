package user.zchp.general.utils;
import user.zchp.utils.SpringContextUtil;

/**
 * spring资源
 *
 * @author:Administrator
 * @create 2018-09-27 15:34
 */
public class SpringResource {
    public static JdbcUtil getJdbcUtil(){
        JdbcUtil jdbcUtil = SpringContextUtil.getBean("jdbcUtil");
        return jdbcUtil;
    }
    public static PropertiesUtil getPropertiesUtil(){
        PropertiesUtil propertiesUtil = SpringContextUtil.getBean("propertiesUtil");
        return propertiesUtil;
    }
}
