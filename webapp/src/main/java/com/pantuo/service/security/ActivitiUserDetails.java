package com.pantuo.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pantuo.dao.pojo.JpaFunction;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UType;
import com.pantuo.mybatis.domain.BusFunction;

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
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			ActivityAuthority that = (ActivityAuthority) o;

			if (auth != null ? !auth.equals(that.auth) : that.auth != null)
				return false;

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

		if (user.utype == null || user.utype == UType.screen) {
			if (user.getGroups() != null) {
				for (Group g : user.getGroups()) {
					auths.add(new ActivityAuthority(g));
				}
			}
		} else if (user.utype == UType.body) {
			if (user.getFunctions() != null) {
				for (BusFunction g : user.getFunctions()) {
					auths.add(new ActivityAuthority(g.getFuncode()));
				}
			}

		}
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return auths;
	}

	public boolean hasAuthority(String groupName) {
		return auths.contains(new ActivityAuthority(groupName));
	}

	/**
	 * 
	 * 判断是否只有参数传递的这几个权限
	 *
	 * @param groupName
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public boolean hasOnlyAuthority(String... groupName) {
		boolean result = false;
		if (groupName.length > 0) {
			List<ActivityAuthority> exist = new ArrayList<ActivityAuthority>(groupName.length);
			for (String waitAdd : groupName) {
				exist.add(new ActivityAuthority(waitAdd));
			}
			result = auths.containsAll(exist) && auths.size() == groupName.length;
		}
		return result;
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
