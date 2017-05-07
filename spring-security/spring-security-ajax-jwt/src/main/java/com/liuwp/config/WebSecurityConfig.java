package com.liuwp.config;


import com.liuwp.jwt.JwtTokenService;
import com.liuwp.jwt.auth.SkipPathRequestMatcher;
import com.liuwp.jwt.auth.TokenAuthenticationProcessingFilter;
import com.liuwp.jwt.auth.TokenAuthenticationProvider;
import com.liuwp.jwt.handler.RestAuthenticationEntryPoint;
import com.liuwp.jwt.login.AjaxLoginAuthenticationProvider;
import com.liuwp.jwt.login.AjaxLoginProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

/**
 * WebSecurityConfig
 *
 * @author vladimir.stankovic
 *         <p>
 *         Aug 3, 2016
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.liuwp.jwt")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    private AjaxLoginAuthenticationProvider ajaxAuthenticationProvider;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Autowired
    private  JwtTokenService tokenService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(tokenAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // We don't need CSRF for JWT based authentication
                .exceptionHandling()
                .authenticationEntryPoint(this.restAuthenticationEntryPoint)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login end-point
                .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token refresh end-point
                .and()
                .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected API End-points
                .and()
                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    public TokenAuthenticationProcessingFilter tokenProcessingFilter() throws Exception {
        List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
        TokenAuthenticationProcessingFilter filter = new TokenAuthenticationProcessingFilter(matcher, tokenService, failureHandler);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }


}
