package user.zchp.dao;

import user.zchp.models.Permission;
import user.zchp.utils.PageParam;
import user.zchp.utils.QueryParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 权限dao层
 *
 * @author:Administrator
 * @create 2018-01-19 18:12
 */
@Resource
public interface PermissionDao extends Dao{
    List<Permission> findMenuList();
    List<Permission> findPermissions(List<String> roles);
    List<Permission> findPermissionByDepth(int depth);
    Permission getMaxSortChild(QueryParam queryParam);
    List<HashMap> queryHashMap(PageParam pageParam);
}
