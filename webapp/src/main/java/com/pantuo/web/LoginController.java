package com.pantuo.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.UserService;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller
public class LoginController {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private IdentityService identityService;
    
    @RequestMapping(value = "/login", produces = "text/html;charset=utf-8")
    public String login(HttpServletRequest request)
    {
        return "login";
    }
    @RequestMapping(value = "/register", produces = "text/html;charset=utf-8")
    public String register(HttpServletRequest request)
    {
        return "register";
    }
    @RequestMapping(value="/loginin",method={RequestMethod.POST,RequestMethod.GET})
	public String loginin(@RequestParam("username")String username,@RequestParam("password")String password,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		String forword="";
		if((username!=null&&username.length()>0)&&(password!=null&&password.length()>0)){
			boolean b = identityService.checkPassword(username, password);
			if(b){
				
				UserDetail user = userService.findByUsername(username);
				request.getSession().setAttribute(com.pantuo.util.Constants.SESSION_U_KEY, user);
				redirectAttributes.addFlashAttribute("message", "登录成功!");
				forword="/contract/contractEnter";//main.jsp

			}else{
				redirectAttributes.addFlashAttribute("message", "用户名或密码错误!");
				forword="/login";//login.jsp
			}
		}else{
			forword="/login";//login.jsp
			redirectAttributes.addFlashAttribute("message", "用户名或密码不能为空!");
		}
		return  "redirect:"+forword;
	}
	
    @RequestMapping(value = "/logout", produces = "text/html;charset=utf-8")
    public String logout(HttpServletRequest request)
    {
        try {
        	
            request.logout();
        } catch (ServletException e) {
            log.error("Failed to logout.", e);
        }

        return "redirect:/login";
    }
    public static void main(String[] args) {
		System.out.println(1);
	}

}
