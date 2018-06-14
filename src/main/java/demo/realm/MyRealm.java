package demo.realm;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import demo.model.User;
import demo.service.UserService;  
  
  
  
public class MyRealm extends AuthorizingRealm {  
  
	@Resource  
    private UserService userService;  
	
    /** 
     * 授权 
     */  
      
    @Override  
    protected AuthorizationInfo doGetAuthorizationInfo(  
            PrincipalCollection principals) {  
        String currentUsername = (String)super.getAvailablePrincipal(principals);   
          
//      List<String> roleList = new ArrayList<String>();    
//      List<String> permissionList = new ArrayList<String>();    
//      //从数据库中获取当前登录用户的详细信息    
//      User user = userService.getByUsername(currentUsername);    
//      if(null != user){    
//          //实体类User中包含有用户角色的实体类信息    
//          if(null!=user.getRoles() && user.getRoles().size()>0){    
//              //获取当前登录用户的角色    
//              for(Role role : user.getRoles()){    
//                  roleList.add(role.getName());    
//                  //实体类Role中包含有角色权限的实体类信息    
//                  if(null!=role.getPermissions() && role.getPermissions().size()>0){    
//                      //获取权限    
//                      for(Permission pmss : role.getPermissions()){    
//                          if(!StringUtils.isEmpty(pmss.getPermission())){    
//                              permissionList.add(pmss.getPermission());    
//                          }    
//                      }    
//                  }    
//              }    
//          }    
//      }else{    
//          throw new AuthorizationException();    
//      }    
//      //为当前用户设置角色和权限    
//      SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();    
//      simpleAuthorInfo.addRoles(roleList);    
//      simpleAuthorInfo.addStringPermissions(permissionList);   
          
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();  
          
        if(null!=currentUsername && currentUsername.equals("admin")){    
            //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色      
            simpleAuthorInfo.addRole("admin");    
            simpleAuthorInfo.addRole("guest");    
            //添加权限    
            simpleAuthorInfo.addStringPermission("admin:manage1");    
            simpleAuthorInfo.addStringPermission("admin:manage2");    
            System.out.println("已为用户[admin]赋予了[admin]和[guest]角色以及[admin:manage1]和[admin:manage2]权限");    
            return simpleAuthorInfo;    
        }   
        
        if(null!=currentUsername && currentUsername.equals("user")){    
        	//添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色      
        	simpleAuthorInfo.addRole("admin");    
        	//添加权限    
        	simpleAuthorInfo.addStringPermission("admin:manage1");    
            System.out.println("已为用户[user]赋予了[admin]角色和[admin:manage1]权限");    
        	return simpleAuthorInfo;    
        }   
        return simpleAuthorInfo;    
      
        //若该方法什么都不做直接返回null的话,就会导致任何用户访问/admin/listUser.jsp时都会自动跳转到unauthorizedUrl指定的地址    
        //详见applicationContext.xml中的<bean id="shiroFilter">的配置    
//      return null;  
    }   
  
    /** 
     * 认证 
     */  
    @Override  
    protected AuthenticationInfo doGetAuthenticationInfo(  
            AuthenticationToken authcToken) throws AuthenticationException {  
          
    	String username = (String) authcToken.getPrincipal();  
        // 调用userService查询是否有此用户  
        User user = userService.getUserByName(username);  
        if (user == null) {  
            // 抛出 帐号找不到异常  
            throw new UnknownAccountException();  
        }  
        // 判断帐号是否锁定  
        if (Boolean.TRUE.equals(user.getLocked())) {  
            // 抛出 帐号锁定异常  
            throw new LockedAccountException();  
        }  
  
        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配  
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(  
                user.getUserName(), // 用户名  
                user.getPassword(), // 密码  
                ByteSource.Util.bytes(user.getCredentialsSalt()),// salt=username+salt  
                getName() // realm name  
        );  
        return authenticationInfo;
    }  
  
    @Autowired //注入父类的属性，注入加密算法匹配密码时使用
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher){
        super.setCredentialsMatcher(credentialsMatcher);
    }
    
}  