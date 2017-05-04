package com.liuwp.auth.entity;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

/**
 * @author vladimir.stankovic
 *         <p>
 *         Aug 4, 2016
 */
public class UserContext {
    private final String username;
    private final List<GrantedAuthority> authorities;

    private UserContext(String username, List<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public static UserContext create(String username, List<GrantedAuthority> authorities) {
        if (StringUtils.isEmpty(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, authorities);
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
