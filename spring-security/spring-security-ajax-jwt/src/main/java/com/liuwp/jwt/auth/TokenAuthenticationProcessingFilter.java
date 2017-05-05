package com.liuwp.jwt.auth;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author:liuwp
 * Date: 2017/5/5
 * Description:验证token是否有效
 */
public class TokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    public static String HEADER_PREFIX = "Bearer ";

    protected TokenAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String header = request.getHeader(JWT_TOKEN_HEADER_PARAM);
        if (StringUtils.isEmpty(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }
        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }
        String token=header.substring(HEADER_PREFIX.length(), header.length());

        return null;
    }
}
