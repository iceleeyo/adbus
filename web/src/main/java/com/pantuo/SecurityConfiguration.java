package com.pantuo;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UType;
import com.pantuo.service.CityService;
import com.pantuo.service.DataInitializationService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.ActivitiUserDetails;
import com.pantuo.service.security.ActivitiUserDetailsService;
import com.pantuo.service.security.Request;
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
	private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
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

	@Value("${sys.type}")
	private String isBodySys;

	@Value("${login.error}")
	private String loginError;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//DaoAuthenticationProvider obj = new DaoAuthenticationProvider();
		//obj.setUserDetailsService(userDetailsService);
		//obj.setHideUserNotFoundExceptions(false);
		//auth.authenticationProvider(obj);
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/*.html", "/login", "/logout", "/homepage/**", "/css/**", "/images/**", "/imgs/**",
						"/js/**", "/index_js/**", "/index_img/**", "/index_css/**", "/style/**", "/upload_temp/**")
				.permitAll()
				.antMatchers("/favicon.ico","/code", "/login_bus", "/busselect/work**/**", "/intro**","/client**", "/about-me", "/media",
						"/effect", "*/media**", "*/effect**", "*/partner**", "/partner", "*/aboutme**", "/aboutme","/caseMore**",
						"/loginForLayer", "/index", "/backend**", "/screen", "/secondLevelPage", "/secondLevelPageBus",
						"/body", "/**/public**/**", "/**/public**", "/register", "/user/**", "/doRegister",
						"/validate/**", "/f/**", "/product/d/**", "/product/c/**", "/product/sift**",
						"/product/sift_data", "/carbox/sift_body", "/product/ajaxdetail/**", "/order/iwant/**",
						"/order/ibus/**","/icbc**","/urs"
						//---add open api
						,"/product/saveBusOrderDetail**"
						,"/product/ajax-BusOrderDetailV2**"
						,"/product/productV2_list**"
						,"/product/acountPrice**"
						,"/product/saveBodyCombo**"
						,"/carbox/ajax-myCards**"
						,"/carbox/ajax-bodyOrderLog**"
						,"/product/changeStats/**"
						,"/product/saveProductV2**"
						,"/upload/saveSimpleFile**"
						,"/product/getBodyProViewJson/**"
						,"/product/ajax-productV2_list**"
						,"/carbox/carTask**"
						,"/carbox/ajax-queryCarBoxBody/**"
						,"/carbox/queryCarHelperyByid/**"
						,"/carbox/editCarHelper**").permitAll().antMatchers("/**").authenticated().anyRequest().permitAll().and()
				.formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/order/myTask/1")
				.successHandler(new SimpleRoleAuthenticationSuccessHandler())
				.failureHandler(new SimpleRoleAuthenticationFailHandler())
				//.failureHandler((new SecurityCustomException()))
				.usernameParameter("username").passwordParameter("password").and().logout()
				.addLogoutHandler(new LogoutHandler() {
					@Override
					public void logout(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) {
						Authentication r = SecurityContextHolder.getContext().getAuthentication();

						UserDetail udetail = null;
						if (r != null) {
							ActivitiUserDetails v = (ActivitiUserDetails) r.getPrincipal();
							if (v != null) {
								udetail = ((ActivitiUserDetails) authentication.getPrincipal()).getUserDetail();
							}

						}
						log.info("logout : {} ", udetail != null ? udetail.getId() : StringUtils.EMPTY);
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

							if (udetail != null && udetail.getUtype() == UType.pub) {
								request.getSession().setAttribute("UType", UType.pub.name());
							}
						}
					}
				})

				.logoutSuccessUrl("/login?logout=relogin").invalidateHttpSession(false).and().csrf().disable();
	}

	class SimpleRoleAuthenticationFailHandler implements AuthenticationFailureHandler {
		private final Logger logger = LoggerFactory.getLogger(SimpleRoleAuthenticationSuccessHandler.class);
		private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {
			//如果是车身
			//if (StringUtils.contains(isBodySys, "body") && exception instanceof BadCredentialsException) {
			if (exception instanceof BadCredentialsException) {
				BadCredentialsException new_name = (BadCredentialsException) exception;
				if (StringUtils.contains(new_name.getMessage(), "Bad")) {
					request.getSession().setAttribute("reLoginMsg", "密码错误!");
				}
			} else if (exception instanceof UsernameNotFoundException) {
				request.getSession().setAttribute("reLoginMsg", exception.getMessage());
			}
			handle(request, response);
		}

		protected void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
			String targetUrl = determineTargetUrl(request, response);

			if (response.isCommitted()) {
				logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
				return;
			}
			redirectStrategy.sendRedirect(request, response, targetUrl);
		}

		protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
			if (StringUtils.contains(isBodySys, "body")) {
				return "/login_bus?error=relogin";
			}
			String referer = request.getHeader("referer");
			log.info("SimpleRoleAuthenticationFailHandler ,referer :{}", referer);
			if (StringUtils.contains(referer, "/backend")) {
				return "/backend?error=relogin";
			} else {
				log.info("for debug determineTargetUrl:1");
				return "/login?error=relogin";
			}
		}

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
			String targetUrl = determineTargetUrl(authentication, request, response);

			if (response.isCommitted()) {
				logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
				return;
			}

			redirectStrategy.sendRedirect(request, response, targetUrl);
		}

		public void clearLogin(Authentication r, HttpServletRequest request) {

			if (r != null) {
				r.setAuthenticated(false);
			}
			SecurityContextHolder.clearContext();

			HttpSession session = request.getSession(false);
			if (session != null) {
				log.info("clearlogin");
				if (session != null) {
					session.invalidate();
				}
				request.getSession().setAttribute("reLoginMsg", loginError);
			}
		}

		protected String determineTargetUrl(Authentication authentication, HttpServletRequest request,
				HttpServletResponse response) {
			boolean isBody = false;
			boolean isBodysales = false, isUserAdmin = false;
			String userName = StringUtils.EMPTY;
			if (authentication.getPrincipal() instanceof ActivitiUserDetails) {
				userName = (((ActivitiUserDetails) authentication.getPrincipal()).getUserDetail().getUsername());
			}

			UserDetail udetail = null;
			ActivitiUserDetails v = (ActivitiUserDetails) authentication.getPrincipal();
			if (v != null) {
				udetail = ((ActivitiUserDetails) authentication.getPrincipal()).getUserDetail();
			}

			if (udetail != null) {

				String referer = request.getHeader("referer");

				log.info("referer :{}, utype:{}", referer, udetail.getUtype());
				if (StringUtils.contains(referer, "/login_bus") && udetail.getUtype() != UType.body) {
					clearLogin(authentication, request);
					return "/login_bus";
				} else if (StringUtils.contains(referer, "/backend") && udetail.getUtype() != UType.screen) {
					clearLogin(authentication, request);
					return "/backend";
				} else if (StringUtils.contains(referer, "/login") && !StringUtils.contains(referer, "/login_bus")
						&& udetail.getUtype() != UType.pub) {
					clearLogin(authentication, request);
					log.info("for debug determineTargetUrl:2");
					return "/login";
				}
				log.info("login success u:{}", udetail.getId());
			}

			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			Set<String> authority = new HashSet<String>();
			
			for (GrantedAuthority grantedAuthority : authorities){
				authority.add(grantedAuthority.getAuthority());
			}
				
			for (GrantedAuthority grantedAuthority : authorities) {
				
				
				
				//如果是车身销售员 到我的订单 没有待办事项
				if (StringUtils.startsWith(grantedAuthority.getAuthority(), "bodysales")) {
					isBodysales = true;
					break;
				} else if (StringUtils.startsWith(grantedAuthority.getAuthority(), "body")
						|| DataInitializationService.bodyAuthSet.contains(grantedAuthority.getAuthority())) {
					isBody = true;
					break;
				} else if (StringUtils.startsWith(grantedAuthority.getAuthority(), "UserManager")
						|| StringUtils.startsWith(grantedAuthority.getAuthority(), "body_roleManager")) {
					isUserAdmin = true;
					break;
				}
			}
			request.getSession().setAttribute("_utype", udetail.getUtype().name());
			request.getSession().setAttribute("UType", udetail.getUtype().name());

			if(authority.contains("ShibaFinancialManager") && authority.size()==1){
				//makeCookieRight(request, response, false);
				return "/order/planContract";
			}
			if (isBodysales) {
				makeCookieRight(request, response, false);
				return "/user/qualification";
			} else if (isBody) {
				makeCookieRight(request, response, false);
				return "/user/qualification";
			} else if (isUserAdmin) {
				return "user/list";
			} else {
				makeCookieRight(request, response, true);
				return "/order/myTask/1";
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
