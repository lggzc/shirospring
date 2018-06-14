package demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import demo.service.UserService;  
  
  
@Controller  
@RequestMapping("/user")  
public class UserController {  
      
	@Autowired
	private UserService userService;
	
	@RequiresRoles("guest")
    @RequestMapping("/guest")  
    public void guest(HttpServletRequest request,Model model){  
        System.out.println("guest");  
    }  
	@RequiresRoles("admin")
	@RequestMapping("/admin")  
	public void admin(HttpServletRequest request,Model model){  
		System.out.println("admin");  
	}  
	@RequiresPermissions("admin:manage1")
	@RequestMapping("/manage1")  
	public void manage1(HttpServletRequest request,Model model){  
		System.out.println("manage1");  
	}  
	@RequiresPermissions("admin:manage1")
	@RequestMapping("/manage2")  
	public void manage2(HttpServletRequest request,Model model){  
		System.out.println("manage2");  
	}  
	@RequestMapping("/getUser")  
	public void getUserByName(HttpServletRequest request,Model model){  
		System.out.println(userService.getUserByName("gong"));  
	}  
//	public static void main(String[] args) {  
//	    String algorithmName = "md5";  
//	    String username = "gong";  
//	    String password = "123456";  
//	    String salt1 = username;  
//	    String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();  
//	    int hashIterations = 3;  
//	    SimpleHash hash = new SimpleHash(algorithmName, password,  
//	            salt1 + salt2, hashIterations);  
//	    String encodedPassword = hash.toHex();  
//	    System.out.println(encodedPassword);  
//	    System.out.println(salt2);  
//	}  
}  