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
                //�ڵ�����login������,SecurityManager���յ�AuthenticationToken,�����䷢�͸������õ�Realmִ�б������֤���    
                //ÿ��Realm�����ڱ�Ҫʱ���ύ��AuthenticationTokens������Ӧ    
                //������һ���ڵ���login(token)����ʱ,�����ߵ�MyRealm.doGetAuthenticationInfo()������,������֤��ʽ����˷���    
                currentUser.login(token);    
            }catch(UnknownAccountException uae){    
            	//�˻������ڵĲ���
                model.addAttribute("error_msg", "�˻�������");  
            }catch(IncorrectCredentialsException ice){    
            	//���벻��ȷ
                model.addAttribute("error_msg", "���벻��ȷ");    
            }catch(LockedAccountException lae){    
            	//��¼ʧ�ܶ�Σ��˻�����10����
                model.addAttribute("error_msg", "��¼ʧ�ܶ�Σ��˻�����10����");    
            }catch (ExcessiveAttemptsException e) {  
            	//�û���������
                model.addAttribute("error_msg", "�û���������");    
            }catch(AuthenticationException ae){    
            	//�޷��жϵ�����
                //ע�⣺���������ں��棬��Ϊ����쳣���Դ���������֤ʧ�ܵ����  
                model.addAttribute("error_msg", "�޷��жϵĴ���");    
            }    
            //��֤�Ƿ��¼�ɹ�    
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