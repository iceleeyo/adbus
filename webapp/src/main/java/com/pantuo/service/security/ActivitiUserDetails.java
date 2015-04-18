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

       
        public String getAuthority() {
            return auth;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ActivityAuthority that = (ActivityAuthority) o;

            if (auth != null ? !auth.equals(that.auth) : that.auth != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return auth != null ? auth.hashCode() : 0;
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

     
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auths;
    }

    public boolean hasAuthority (String groupName) {
        return auths.contains(new ActivityAuthority(groupName));
    }

    public UserDetail getUserDetail() {
        return user;
    }

    public User getUser() {
        return user.getUser();
    }

    public String getPassword() {
        return user.getUser().getPassword();
    }

    
    public String getUsername() {
        return user.getUsername();
    }

    
    public boolean isAccountNonExpired() {
        return true;
    }

   
    public boolean isAccountNonLocked() {
        return true;
    }

   
    public boolean isCredentialsNonExpired() {
        return true;
    }

     
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
