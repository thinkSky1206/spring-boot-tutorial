package com.liuwp.jwt.auth;

import com.liuwp.jwt.entity.UserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Author: liuwuping
 * Date: 17/5/6
 * Description:
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private UserContext userContext;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.setAuthenticated(false);

    }


    public JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userContext = userContext;
        super.setAuthenticated(true);
    }


    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userContext;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }

}
