package com.pantuo.web;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import scala.actors.threadpool.Arrays;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UType;
import com.pantuo.service.MailService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.ActivitiUserDetailsService;
import com.pantuo.util.Pair;
import com.pantuo.service.security.Request;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller
public class LoginController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserServiceInter userService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private MailService mailService;
	@Autowired
	private ActivitiUserDetailsService authUserService;
	@Value("${sys.type}")
	private String isBodySys;

	@RequestMapping(value = "/loginForLayer")
	@ResponseBody
	public Pair<Boolean, String> loginForLayer(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpServletRequest request, Authentication auth) {
		return userService.loginForLayer(request, username, password);
	}

	@RequestMapping(value = "/login_bus", produces = "text/html;charset=utf-8")
	public String login_bus(Model model, HttpServletRequest request, Authentication auth) {
		Object asObject = request.getSession().getAttribute("medetype");
		if (asObject == null) {
			request.getSession().setAttribute("medetype", "body");
		}
		if (auth != null && auth.isAuthenticated()) {
			return "redirect:/busselect/myTask/1";
		} else {
			model.addAttribute("msg", "用户名或密码错误");
			return "login_bus";
		}
	}
	@RequestMapping(value = "/wbm", produces = "text/html;charset=utf-8")
	public String wbm(Model model, HttpServletRequest request, Authentication auth) {
		return backend(model, request, auth);
	}
	@RequestMapping(value = "/backend", produces = "text/html;charset=utf-8")
	public String login(Model model, HttpServletRequest request, Authentication auth) {
		return backend(model, request, auth);
	}

	private String backend(Model model, HttpServletRequest request, Authentication auth) {
		log.info("---backend-----:{}" ,Request.getIpAddr(request));
		if (StringUtils.equals(isBodySys, "body")) {
			return "redirect:/login_bus";
		}
		if (StringUtils.equals("body", (String) request.getSession().getAttribute("_utype"))) {
			return "login_bus";
		}
		Object asObject = request.getSession().getAttribute("medetype");
		if (asObject == null) {
			request.getSession().setAttribute("medetype", "screen");
		}
		//clearErrorMsg(request);
		if (auth != null && auth.isAuthenticated()) {
			return "redirect:/order/myTask/1";
		} else {
			model.addAttribute("msg", "用户名或密码错误");
			return "login";
		}
	}

	@RequestMapping(value = "/login", produces = "text/html;charset=utf-8")
	public String logMini(Model model, HttpServletRequest request, Authentication auth) {
		log.info("---login-----:{},referer:{}", Request.getIpAddr(request), request.getHeader("referer"));
		if (StringUtils.equals(isBodySys, "body")) {
			return "redirect:/login_bus";
		}

		//clearErrorMsg(request);
		if (StringUtils.isBlank(request.getParameter("logout"))) {
			return "logMini";
		} else {
			if (StringUtils.equals("relogin", (String) request.getParameter("logout"))
					&& StringUtils.equals(UType.pub.name(), (String) request.getSession().getAttribute("UType"))) {
				return "logMini";
			} else {
				return "redirect:/wbm";
			}
		}

	}

	private void clearErrorMsg(HttpServletRequest request) {
		if (StringUtils.isBlank(request.getParameter("error"))) {
			try {
				request.getSession().removeAttribute("reLoginMsg");
			} catch (Exception e) {
			}
		}
	}

	@RequestMapping(value = "/logout", produces = "text/html;charset=utf-8")
	public String logout(HttpServletRequest request) {
		try {
			request.logout();
		} catch (ServletException e) {
			log.error("Failed to logout.", e);
		}
		if (StringUtils.isBlank(request.getParameter("error"))) {

		}
		log.info("---logout-----:{}" ,Request.getIpAddr(request));
		return "redirect:/login";
	}

	@RequestMapping(value = "/register", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
	public String register(HttpServletRequest request) {
		return "register";
	}

	@RequestMapping(value = "/doRegister", method = { RequestMethod.POST })
	@ResponseBody
	public UserDetail createUser(UserDetail detail, HttpServletRequest request, Principal principal) {
		com.pantuo.util.BeanUtils.filterXss(detail);
		detail.setUtype(UserDetail.UType.pub);
		detail.setStringGroups(Arrays.asList(new String[] { "advertiser" }));
		boolean success = userService.createUserFromPage(detail, request, principal);
		if (success) {
			mailService.sendActivateMail(detail);
			//            //login user
			//            UserDetails newUser = authUserService.loadUserByUsername(detail.getUsername());
			//            Authentication auth = new UsernamePasswordAuthenticationToken(newUser, newUser.getPassword(), newUser.getAuthorities());
			//            SecurityContextHolder.getContext().setAuthentication(auth);
		}
		return detail;
	}

	@RequestMapping(value = "/loginin", method = { RequestMethod.POST, RequestMethod.GET })
	public String loginin(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String forword = "";
		if ((username != null && username.length() > 0) && (password != null && password.length() > 0)) {
			boolean b = identityService.checkPassword(username, password);
			if (b) {

				UserDetail user = userService.findByUsername(username);
				request.getSession().setAttribute(com.pantuo.util.Constants.SESSION_U_KEY, user);
				redirectAttributes.addFlashAttribute("message", "登录成功!");
				forword = "/contract/contractEnter";//main.jsp

			} else {
				redirectAttributes.addFlashAttribute("message", "用户名或密码错误!");
				forword = "/login";//login.jsp
			}
		} else {
			forword = "/login";//login.jsp
			redirectAttributes.addFlashAttribute("message", "用户名或密码不能为空!");
		}
		return "redirect:" + forword;
	}

	public static void main(String[] args) {
		System.out.println(1);
	}

}
