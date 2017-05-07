package com.liuwp.jwt.auth;

import com.liuwp.jwt.Constant;
import com.liuwp.jwt.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
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

    private AuthenticationFailureHandler failureHandler;
    private JwtTokenService tokenService;


    public TokenAuthenticationProcessingFilter(RequestMatcher requestMatcher, JwtTokenService tokenService, AuthenticationFailureHandler failureHandler) {
        super(requestMatcher);
        this.tokenService = tokenService;
        this.failureHandler = failureHandler;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String header = request.getHeader(Constant.JWT_TOKEN_HEADER_PARAM);
        String token = tokenService.extractHeadToken(header);
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
