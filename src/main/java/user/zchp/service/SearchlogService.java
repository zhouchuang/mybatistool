package user.zchp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.zchp.dao.SearchlogDao;

/**
 * 搜索
 *
 * @author zhouchuang
 * @create 2018-05-11 22:13
 */
@Service("searchlogService")
public class SearchlogService extends AbstractService {
    @Autowired
    private SearchlogDao searchlogDao;

    public SearchlogService() {
        super();
    }

    @Override
    public SearchlogDao getDao() {
        return this.searchlogDao;
    }

}
