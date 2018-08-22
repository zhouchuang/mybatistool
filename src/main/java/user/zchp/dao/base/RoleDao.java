package user.zchp.dao.base;

import user.zchp.models.base.Role;
import user.zchp.utils.PageParam;

import java.util.HashMap;
import java.util.List;

/**
 * 角色dao层
 *
 * @author:Administrator
 * @create 2018-01-19 18:12
 */
public interface RoleDao   extends Dao{
    List<Role> findRolesByAccountNo(String accountNo);
    List<HashMap> queryHashMap(PageParam pageParam);
}
