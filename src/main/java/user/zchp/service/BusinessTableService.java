package user.zchp.service;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.zchp.dao.Dao;
import user.zchp.dao.TableDao;
import user.zchp.general.resource.AbstractResource;
import user.zchp.utils.StringUtils;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * 表service层
 *
 * @author zhouchuang
 * @create 2018-05-12 14:46
 */
@Service("tableService")
public class BusinessTableService  extends AbstractResource{


    public List<String>  tableList(String database){
        List<String> list = new ArrayList<String>();
        Connection  conn = null;
        try {
            conn = getConn();
            Statement colmunment = conn.createStatement();
            ResultSet result = colmunment.executeQuery("select table_name from information_schema.TABLES where TABLE_SCHEMA= \'"+database+"\' order by table_name ");
            while(result.next()){
                String tableName = result.getString("table_name");
                list.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConn(conn);
        }
        return list;
    }

    public List<Map> fieldList(String table){
        List<Map> list = new ArrayList<Map>();
        Connection conn = null;
        try {
            conn = getConn();
            Statement colmunment = conn.createStatement();
            ResultSet result = colmunment.executeQuery("show full fields from "+table);
            while(result.next()){
                Map map = new HashMap();
                map.put("Field",result.getString("Field"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConn(conn);
        }
        return list;
    }

}
