package com.pantuo;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.UserDetail.UType;
import com.pantuo.service.CityService;
import com.pantuo.service.DataInitializationService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.ActivitiUserDetails;
import com.pantuo.service.security.ActivitiUserDetailsService;
import com.pantuo.util.Request;
import com.pantuo.web.ControllerSupport;

/**
 * Web security
 *
 * @author tliu
 */

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
/*panxh method security*/
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	/*
	    @Autowired
	    private UserDetailRepository userRepo;

	    @Autowired
	    private RoleRepository roleRepo;

	    @Autowired
	    private UserRoleRepository userRoleRepo;
	*/
	@Autowired
	private UserServiceInter userService;
	@Autowired
	private CityService cityService;

	@Autowired
	private ActivitiUserDetailsService userDetailsService;

	//@Autowired
	//private DaoAuthenticationProvider daoAuthenticationProvider;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//DaoAuthenticationProvider obj = new DaoAuthenticationProvider();
		//obj.setUserDetailsService(userDetailsService);
		//obj.setHideUserNotFoundExceptions(false);
		//auth.authenticationProvider(obj);

		auth.userDetailsService(userDetailsService);
		/*        auth
		                .jdbcAuthentication()
		                .dataSource(dataSource)
		                .usersByUsernameQuery(
		                        "select username, password, enabled from UserDetail where username=?")
		                .authoritiesByUsernameQuery(
		                        "select username, role from UserRole where username=?");


		        if (userRepo.findByUsername("admin").isEmpty()) {
		            List<UserDetail> users = new ArrayList<UserDetail> ();
		            users.add(new UserDetail("admin", "123456"));
		            users.add(new UserDetail("liuchao", "abcdef"));
		            userRepo.save(users);
		        }
		        if (roleRepo.findByName("admin").isEmpty()) {
		            List<Role> roles = new ArrayList<Role>();
		            roles.add(new Role("admin", "super user"));
		            roles.add(new Role("user", "normal user"));
		            roleRepo.save(roles);
		        }
		        if (userRoleRepo.findByUsername("admin").isEmpty()) {
		            List<UserRole> userRoles = new ArrayList<UserRole>();
		            userRoles.add(new UserRole("admin", "user"));
		            userRoles.add(new UserRole("admin", "admin"));
		            userRoleRepo.save(userRoles);
		        }*/
		//        auth.inMemoryAuthentication()
		//                .withUser("admin").password("123$%^").roles("USER");

	}

	//.csrf() is optional, enabled by default, if using WebSecurityConfigurerAdapter constructor
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/*.html", "/login", "/logout", "/homepage/**", "/css/**", "/images/**", "/imgs/**",
						"/js/**", "/index_js/**", "/index_img/**", "/index_css/**", "/style/**","/upload_temp/**")
				.permitAll()
				.antMatchers("/login_bus","/busselect/work**/**", "/intro**", "/about-me", "/media", "/effect","*/media**","*/effect**", 
						"*/partner**","/partner","*/aboutme**","/aboutme","/loginForLayer","/index","/logMini**",
						"/screen", "/secondLevelPage", "/secondLevelPageBus", "/body", "/**/public**/**",
						"/**/public**", "/register", "/user/**", "/doRegister", "/validate/**", "/f/**",
						"/product/d/**", "/product/c/**", "/product/sift**", "/product/sift_data", "/carbox/sift_body",
						"/product/ajaxdetail/**", "/order/iwant/**", "/order/ibus/**")
				.permitAll()
				.antMatchers("/**")
				.authenticated()
				.anyRequest()
				.permitAll()
				//.antMatchers("/user/enter").access("hasRole('ShibaOrderManager')")
				//192.168.1.105/busselect/workList/1439893707748/4
				//http://www.baeldung.com/spring_redirect_after_login
				.and().formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/order/myTask/1")
				.successHandler(new SimpleRoleAuthenticationSuccessHandler())
				//.failureHandler((new SecurityCustomException()))
				.usernameParameter("username").passwordParameter("password").and().logout()
				.addLogoutHandler(new LogoutHandler() {
					@Override
					public void logout(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) {
						Authentication r = SecurityContextHolder.getContext().getAuthentication();
						if (r != null) {
							r.setAuthenticated(false);
						}
						SecurityContextHolder.clearContext();
						HttpSession session = request.getSession(false);
						if (session != null) {
							String tString = (String) session.getAttribute("medetype");
							String _utype = (String) session.getAttribute("_utype");
							System.err.println(tString);
							if (session != null) {
								session.invalidate();
							}
							request.getSession().setAttribute("medetype", tString);
							request.getSession().setAttribute("_utype", _utype);
						}
					}
				})

				.logoutSuccessUrl("/login?logout").invalidateHttpSession(false).and().csrf().disable();
	}

	/**
	 * 
	 * <b><code>SimpleRoleAuthenticationSuccessHandler</code></b>
	 * <p>
	 * success handler
	 * </p>
	 * <b>Creation Time:</b> 2015年8月13日 下午7:50:50
	 * @author impanxh@gmail.com
	 * @since pantuo 1.0-SNAPSHOT
	 */
	class SimpleRoleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
		private final Logger logger = LoggerFactory.getLogger(SimpleRoleAuthenticationSuccessHandler.class);
		private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException {
			handle(request, response, authentication);
			clearAuthenticationAttributes(request);
		}

		protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
				throws IOException {
			String targetUrl = determineTargetUrl(authentication,request,response);

			if (response.isCommitted()) {
				logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
				return;
			}

			redirectStrategy.sendRedirect(request, response, targetUrl);
		}

		protected String determineTargetUrl(Authentication authentication, HttpServletRequest request,
				HttpServletResponse response) {
			boolean isBody = false;
			boolean isBodysales = false, isUserAdmin = false;
			String userName = StringUtils.EMPTY;
			if (authentication.getPrincipal() instanceof ActivitiUserDetails) {
				userName = (((ActivitiUserDetails) authentication.getPrincipal()).getUserDetail().getUsername());
			}
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {

				//如果是车身销售员 到我的订单 没有待办事项
				if (StringUtils.startsWith(grantedAuthority.getAuthority(), "bodysales")) {
					request.getSession().setAttribute("_utype", "body");
					isBodysales = true;
					break;
				} else if (StringUtils.startsWith(grantedAuthority.getAuthority(), "body")
						|| DataInitializationService.bodyAuthSet.contains(grantedAuthority.getAuthority())) {
					isBody = true;
					request.getSession().setAttribute("_utype", "body");
					break;
				} else if (StringUtils.startsWith(grantedAuthority.getAuthority(), "UserManager")
						|| StringUtils.startsWith(grantedAuthority.getAuthority(), "body_roleManager")) {
					if (StringUtils.startsWith(userName, "mediaAdmin")) {
						request.getSession().setAttribute("_utype", "screen");
					} else {
						request.getSession().setAttribute("_utype", "body");

					}
					isUserAdmin = true;
					break;
				}
			}
			if (isBodysales) {
				makeCookieRight(request, response, false);
				return "/busselect/myOrders/1";
			} else if (isBody) {
				makeCookieRight(request, response, false);
				return "/busselect/myTask/1";
			} else if (isUserAdmin) {
				return "user/list";
			} else {
				makeCookieRight(request, response, true);
				request.getSession().setAttribute("_utype", "screen");
				return "/order/myTask/1";
				//throw new IllegalStateException();
			}
		}

		protected void clearAuthenticationAttributes(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session == null) {
				return;
			}
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}

		public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
			this.redirectStrategy = redirectStrategy;
		}

		protected RedirectStrategy getRedirectStrategy() {
			return redirectStrategy;
		}

		/**
		 * 
		 * 纠正cookie 
		 *
		 * @param request
		 * @param response
		 * @param isScreen
		 * @return
		 * @since pantuo 1.0-SNAPSHOT
		 */
		public int makeCookieRight(HttpServletRequest request, HttpServletResponse response, boolean isScreen) {
			String cityCookieValue = Request.getCookieValue(request, "city");
			int city = NumberUtils.toInt(cityCookieValue, -1);
			if (isScreen) {
				city = city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city);
			} else {
				city = city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city);
			}

			request.getSession().setAttribute("medetype", isScreen ? "screen" : "body");

			JpaCity r = cityService.fromId(city);
			if (r == null) {
				logger.warn("city:{} is ", city);

				Cookie cookie = new Cookie("city", String.valueOf(city));
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
			int defaultCitye = (isScreen ? ControllerSupport.defaultCookieValue
					: ControllerSupport.defaultCookieValue + 1);

			int w = r == null ? (defaultCitye) : r.getId();
			w = w > 6 ? (defaultCitye) : r.getId();
			try {
				Cookie cookie = new Cookie("city", String.valueOf(w));
				cookie.setPath("/");
				cookie.setMaxAge(604800);
				response.addCookie(cookie);
			} catch (Exception e) {
			}
			return w;
		}
	}
	
	
	
	
	
}
