package user.zchp.service;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.zchp.dao.UserDao;
import user.zchp.dao.ext.UserExtDao;
import user.zchp.models.Permission;
import user.zchp.models.Role;
import user.zchp.models.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户service层
 *
 * @author:Administrator
 * @create 2018-01-18 13:06
 */
@Service("userService")
public class UserService extends AbstractService {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserExtDao userExtDao;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;


    public UserDao getDao() {
        return this.userDao;
    }



    public List<User> findExtList(){
        return  this.userExtDao.findExtList();
    }

    public User getByAccountNo(String accountNo){
        return getDao().getByAccountNo(accountNo);
    }

    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws Exception {

        User authUser = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = this.getByAccountNo(authUser.getAccountNo());
        if(user != null) {;
            // 根据用户名查询当前用户拥有的角色
            Set<Role> roles = roleService.findRoleSetsByAccountNo(user.getAccountNo());
            Set<String> roleNames = new HashSet<String>();
            List<String> roleIds = new ArrayList<String>();
            for (Role role : roles) {
                if(role!=null){
                    roleNames.add(role.getName());
                    roleIds.add(role.getId());
                }
            }
            // 将角色名称提供给info
            authorizationInfo.setRoles(roleNames);
            if(!CollectionUtils.isEmpty(roleIds)){
                // 根据用户名查询当前用户权限
                Set<Permission> permissions = permissionService.findPermissionsToSet(roleIds);
                Set<String> permissionNames = new HashSet<String>();
                for (Permission permission : permissions) {
                    permissionNames.add(permission.getAlias());
                }
                // 将权限名称提供给info
                authorizationInfo.setStringPermissions(permissionNames);
            }
        }
        return authorizationInfo;
    }



}
