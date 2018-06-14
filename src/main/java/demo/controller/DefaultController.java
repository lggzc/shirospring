package demo.controller;

import java.io.Serializable;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;  
  
@Controller  
@RequestMapping("/default")  
public class DefaultController  {  
      
	@Autowired
	CacheManager cacheManager;
	
    @RequestMapping("/login")  
    public String login(String username,String password,Model model){  
         UsernamePasswordToken token = new UsernamePasswordToken(username, password);  
         token.setRememberMe(true);  
         System.out.println("DefaultController.login#token="+token);  
           
         Subject currentUser = SecurityUtils.getSubject();  
           
         try {    
                //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查    
                //每个Realm都能在必要时对提交的AuthenticationTokens作出反应    
                //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法    
                currentUser.login(token);    
            }catch(UnknownAccountException uae){    
            	//账户不存在的操作
                model.addAttribute("error_msg", "账户不存在");  
            }catch(IncorrectCredentialsException ice){    
            	//密码不正确
                model.addAttribute("error_msg", "密码不正确");    
            }catch(LockedAccountException lae){    
            	//登录失败多次，账户锁定10分钟
                model.addAttribute("error_msg", "登录失败多次，账户锁定10分钟");    
            }catch (ExcessiveAttemptsException e) {  
            	//用户被锁定了
                model.addAttribute("error_msg", "用户被锁定了");    
            }catch(AuthenticationException ae){    
            	//无法判断的情形
                //注意：这个必须放在后面，因为这个异常可以处理所有认证失败的情况  
                model.addAttribute("error_msg", "无法判断的错误");    
            }    
            //验证是否登录成功    
            if(currentUser.isAuthenticated()){    
                System.out.println("user[" + username + "]authentication success");   
//                Cache<Serializable, Session> activeSessionCache = cacheManager.getCache("shiro-activeSessionCache");
//                activeSessionCache.put(currentUser.getSession().getId(), currentUser.getSession());
                return "main";  
            }    
            token.clear();   
            return "login";  
           
    }  
    @RequestMapping("/logout")  
    public String logout(){  
        SecurityUtils.getSubject().logout();  
        return "login";  
    }  
    @RequestMapping("/test")  
    public String test(){  
    	return "index";
    }  
}  