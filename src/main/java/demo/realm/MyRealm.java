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
     * ��Ȩ 
     */  
      
    @Override  
    protected AuthorizationInfo doGetAuthorizationInfo(  
            PrincipalCollection principals) {  
        String currentUsername = (String)super.getAvailablePrincipal(principals);   
          
//      List<String> roleList = new ArrayList<String>();    
//      List<String> permissionList = new ArrayList<String>();    
//      //�����ݿ��л�ȡ��ǰ��¼�û�����ϸ��Ϣ    
//      User user = userService.getByUsername(currentUsername);    
//      if(null != user){    
//          //ʵ����User�а������û���ɫ��ʵ������Ϣ    
//          if(null!=user.getRoles() && user.getRoles().size()>0){    
//              //��ȡ��ǰ��¼�û��Ľ�ɫ    
//              for(Role role : user.getRoles()){    
//                  roleList.add(role.getName());    
//                  //ʵ����Role�а����н�ɫȨ�޵�ʵ������Ϣ    
//                  if(null!=role.getPermissions() && role.getPermissions().size()>0){    
//                      //��ȡȨ��    
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
//      //Ϊ��ǰ�û����ý�ɫ��Ȩ��    
//      SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();    
//      simpleAuthorInfo.addRoles(roleList);    
//      simpleAuthorInfo.addStringPermissions(permissionList);   
          
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();  
          
        if(null!=currentUsername && currentUsername.equals("admin")){    
            //���һ����ɫ,�������������ϵ����,����֤�����û�ӵ��admin��ɫ      
            simpleAuthorInfo.addRole("admin");    
            simpleAuthorInfo.addRole("guest");    
            //���Ȩ��    
            simpleAuthorInfo.addStringPermission("admin:manage1");    
            simpleAuthorInfo.addStringPermission("admin:manage2");    
            System.out.println("��Ϊ�û�[admin]������[admin]��[guest]��ɫ�Լ�[admin:manage1]��[admin:manage2]Ȩ��");    
            return simpleAuthorInfo;    
        }   
        
        if(null!=currentUsername && currentUsername.equals("user")){    
        	//���һ����ɫ,�������������ϵ����,����֤�����û�ӵ��admin��ɫ      
        	simpleAuthorInfo.addRole("admin");    
        	//���Ȩ��    
        	simpleAuthorInfo.addStringPermission("admin:manage1");    
            System.out.println("��Ϊ�û�[user]������[admin]��ɫ��[admin:manage1]Ȩ��");    
        	return simpleAuthorInfo;    
        }   
        return simpleAuthorInfo;    
      
        //���÷���ʲô������ֱ�ӷ���null�Ļ�,�ͻᵼ���κ��û�����/admin/listUser.jspʱ�����Զ���ת��unauthorizedUrlָ���ĵ�ַ    
        //���applicationContext.xml�е�<bean id="shiroFilter">������    
//      return null;  
    }   
  
    /** 
     * ��֤ 
     */  
    @Override  
    protected AuthenticationInfo doGetAuthenticationInfo(  
            AuthenticationToken authcToken) throws AuthenticationException {  
          
    	String username = (String) authcToken.getPrincipal();  
        // ����userService��ѯ�Ƿ��д��û�  
        User user = userService.getUserByName(username);  
        if (user == null) {  
            // �׳� �ʺ��Ҳ����쳣  
            throw new UnknownAccountException();  
        }  
        // �ж��ʺ��Ƿ�����  
        if (Boolean.TRUE.equals(user.getLocked())) {  
            // �׳� �ʺ������쳣  
            throw new LockedAccountException();  
        }  
  
        // ����AuthenticatingRealmʹ��CredentialsMatcher��������ƥ��  
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(  
                user.getUserName(), // �û���  
                user.getPassword(), // ����  
                ByteSource.Util.bytes(user.getCredentialsSalt()),// salt=username+salt  
                getName() // realm name  
        );  
        return authenticationInfo;
    }  
  
    @Autowired //ע�븸������ԣ�ע������㷨ƥ������ʱʹ��
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher){
        super.setCredentialsMatcher(credentialsMatcher);
    }
    
}  