package com.pantuo.service.security;

import com.pantuo.dao.pojo.UserDetail;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tliu on 3/22/15.
 */
public class ActivitiUserDetails implements UserDetails {
    private UserDetail user;
    private List<ActivityAuthority> auths;

    private static class ActivityAuthority implements GrantedAuthority {
        private String auth;

        public ActivityAuthority(Group g) {
            if (g != null)
                auth = g.getId();
        }

        public ActivityAuthority(String auth) {
            this.auth = auth;
        }

        @Override
        public String getAuthority() {
            return auth;
        }
    }

    public ActivitiUserDetails(UserDetail user) {
        this.user = user;
        this.auths = new ArrayList<ActivityAuthority>();
        if (user.getGroups() != null) {
            for (Group g : user.getGroups()) {
                auths.add(new ActivityAuthority(g));
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auths;
    }

    @Override
    public String getPassword() {
        return user.getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
