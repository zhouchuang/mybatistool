package user.zchp.dao;

import user.zchp.models.Role;
import user.zchp.utils.PageParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 角色dao层
 *
 * @author:Administrator
 * @create 2018-01-19 18:12
 */
@Resource
public interface RoleDao   extends Dao{
    List<Role> findRolesByAccountNo(String accountNo);
    List<HashMap> queryHashMap(PageParam pageParam);
}
