package com.liuwp.config;

import com.liuwp.auth.CustomSuccessHandler;
import com.liuwp.auth.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * Author: liuwuping
 * Date: 17/4/30
 * Description:
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.liuwp.auth")
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Autowired
    private SimpleUrlAuthenticationFailureHandler customFailHandler;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/resources/**").authenticated()
                .and()
                .formLogin()
                .successHandler(customSuccessHandler)
                .failureHandler(customFailHandler)
                .and()
                .logout();

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("uu").password("123456").roles("admin");
    }


    @Bean
    public CustomSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler customFailureHandler(){
        return new SimpleUrlAuthenticationFailureHandler();
    }

}
