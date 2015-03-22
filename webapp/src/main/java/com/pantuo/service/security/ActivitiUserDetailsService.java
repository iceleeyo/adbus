package com.pantuo.service.security;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.UserService;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tliu on 3/22/15.
 */
@Service
public class ActivitiUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDetail user = userService.getByUsername(username);
            if (user == null)
                throw new UsernameNotFoundException("no user found for username " + username);

            if (user.getUser() == null)
                throw new UsernameNotFoundException("fail to find Activiti user for username " + username);
            return new ActivitiUserDetails(user);
        } catch (Exception ae) {
            throw new UsernameNotFoundException("Fail to find user for username " + username + ", e=" + ae);
        }
    }
}
