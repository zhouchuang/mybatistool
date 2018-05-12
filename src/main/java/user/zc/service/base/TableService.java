package user.zc.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.zc.dao.base.Dao;
import user.zc.dao.base.TableDao;

import java.util.List;
import java.util.Map;

/**
 * 表service层
 *
 * @author zhouchuang
 * @create 2018-05-12 14:46
 */
@Service("tableService")
public class TableService extends AbstractService {
    @Autowired
    TableDao tableDao;

    @Override
    public Dao getDao() {
        return this.tableDao;
    }

    public List<String> tableList(String database){
        return tableDao.tableList(database);
    }

    public List<Map> fieldList(String table){
        return tableDao.fieldList(table);
    }

}
