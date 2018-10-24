package user.zchp.dao.ext;

import user.zchp.dao.UserDao;
import user.zchp.models.User;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户Dao扩展类
 *
 * @author zhouchuang
 * @create 2018-10-24 21:38
 */
@Resource
public interface UserExtDao extends UserDao {
    List<User> findExtList();
}
