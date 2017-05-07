package com.liuwp.jwt.auth;

import com.liuwp.jwt.JwtTokenService;
import com.liuwp.jwt.entity.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: liuwuping
 * Date: 17/5/6
 * Description:
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private JwtTokenService tokenService;


    @Autowired
    public TokenAuthenticationProvider(JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token= (String) authentication.getCredentials();
        Claims jwsClaims = tokenService.parseToken(token);
        String subject = jwsClaims.getSubject();
        List<String> scopes = jwsClaims.get("scopes", List.class);
        List<GrantedAuthority> authorities = scopes.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
//        AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
        UserContext context = UserContext.create(subject, authorities);
        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
            return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
