package user.zchp.dao.base;

import user.zchp.models.base.User;
import user.zchp.utils.PageParam;

import java.util.HashMap;
import java.util.List;

/**
 * 用户Dao层
 *
 * @author:Administrator
 * @create 2018-01-18 13:06
 */
public interface UserDao  extends Dao{
    List<HashMap> queryHashMap(PageParam pageParam);
    User getByAccountNo(String accountNo);
}
