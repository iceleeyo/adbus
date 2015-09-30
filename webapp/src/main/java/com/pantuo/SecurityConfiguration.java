package com.pantuo;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.ActivitiUserDetailsService;

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
						"/js/**", "/index_js/**","/index_img/**","/index_css/**","/style/**")
				.permitAll()
				.antMatchers("/busselect/work**/**","/intro**", "/about-me", "/loginForLayer","/screen", "/body","/**/public**", "/register", "/user/**",
						"/doRegister", "/validate/**", "/f/**", "/product/d/**", "/product/c/**", "/product/sift**",
						"/product/sift_data", "/product/ajaxdetail/**", "/order/iwant/**","/order/ibus/**").permitAll()
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
							System.err.println(tString);
							if (session != null) {
								session.invalidate();
							}
							request.getSession().setAttribute("medetype", tString);
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
			String targetUrl = determineTargetUrl(authentication);

			if (response.isCommitted()) {
				logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
				return;
			}

			redirectStrategy.sendRedirect(request, response, targetUrl);
		}

		protected String determineTargetUrl(Authentication authentication) {
			boolean isBody = false;
			boolean isBodysales = false, isUserAdmin = false;

			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {

				//如果是车身销售员 到我的订单 没有待办事项
				if (StringUtils.startsWith(grantedAuthority.getAuthority(), "bodysales")) {
					isBodysales = true;
					break;
				} else if (StringUtils.startsWith(grantedAuthority.getAuthority(), "body")) {
					isBody = true;
					break;
				} else if (StringUtils.startsWith(grantedAuthority.getAuthority(), "UserManager")) {
					isUserAdmin = true;
					break;
				}
			}
			if (isBodysales) {
				return "/busselect/myOrders/1";
			} else if (isBody) {
				return "/busselect/myTask/1";
			} else if (isUserAdmin) {
				return "user/list";
			} else {
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
	}
}
