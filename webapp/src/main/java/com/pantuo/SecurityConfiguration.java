package com.pantuo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

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

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
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
				.antMatchers("/", "/*.html", "/login", "/logout", "/homepage/**", "/css/**", "/images/**", "/imgs/**", "/js/**",
						"/style/**").permitAll().antMatchers("/register","/user/**", "/doRegister", "/validate/**", "/f/**","/product/d/**","/product/c/**").permitAll().antMatchers("/**")
				.authenticated().anyRequest()
				.permitAll()
				//.antMatchers("/user/enter").access("hasRole('ShibaOrderManager')")
				.and().formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/order/myTask/1")
				.usernameParameter("username").passwordParameter("password").and().logout()
				.logoutSuccessUrl("/login?logout").invalidateHttpSession(true).and().csrf().disable();
	}
}
