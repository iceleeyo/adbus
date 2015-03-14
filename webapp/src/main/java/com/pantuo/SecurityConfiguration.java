package com.pantuo;

import com.pantuo.dao.RoleRepository;
import com.pantuo.dao.UserRepository;
import com.pantuo.dao.UserRoleRepository;
import com.pantuo.dao.pojo.Role;
import com.pantuo.dao.pojo.User;
import com.pantuo.dao.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Web security
 *
 * @author tliu
 */

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserRoleRepository userRoleRepo;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username, password, enabled from User where username=?")
                .authoritiesByUsernameQuery(
                        "select username, role from UserRole where username=?");


        if (userRepo.findByUsername("admin").isEmpty()) {
            List<User> users = new ArrayList<User> ();
            users.add(new User("admin", "123456"));
            users.add(new User("liuchao", "abcdef"));
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
        }
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("123$%^").roles("USER");
    }

    //.csrf() is optional, enabled by default, if using WebSecurityConfigurerAdapter constructor
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/*.html", "/doc/**", "/wadl").authenticated()
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .loginPage("/login").failureUrl("/login?error")
                .usernameParameter("username").passwordParameter("password")
                .and()
                .logout().logoutSuccessUrl("/login?logout")
                .and()
                .csrf().disable();
    }
}
