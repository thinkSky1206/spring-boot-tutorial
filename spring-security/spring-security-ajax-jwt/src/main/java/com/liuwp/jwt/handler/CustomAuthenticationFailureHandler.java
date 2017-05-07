package com.liuwp.jwt.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liuwp.jwt.exceptions.AuthMethodNotSupportedException;
import com.liuwp.jwt.exceptions.JwtExpiredTokenException;
import com.liuwp.util.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * @author vladimir.stankovic
 *         <p>
 *         Aug 3, 2016
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    public CustomAuthenticationFailureHandler() {

    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof BadCredentialsException) {
            JsonUtil.getJsonMapper().writeValue(response.getWriter(), "Invalid username or password");
        } else if (e instanceof JwtExpiredTokenException) {
            JsonUtil.getJsonMapper().writeValue(response.getWriter(), "Token has expired");
        } else if (e instanceof AuthMethodNotSupportedException) {
            JsonUtil.getJsonMapper().writeValue(response.getWriter(), e.getMessage());
        } else if (e instanceof AuthenticationServiceException) {
            JsonUtil.getJsonMapper().writeValue(response.getWriter(), e.getMessage());
        }
        JsonUtil.getJsonMapper().writeValue(response.getWriter(), "Authentication failed");
    }
}
