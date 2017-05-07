package com.liuwp.controller;

import com.liuwp.jwt.Constant;
import com.liuwp.jwt.JwtTokenService;
import com.liuwp.jwt.entity.JwtToken;
import com.liuwp.jwt.entity.RefreshToken;
import com.liuwp.jwt.entity.UserContext;
import com.liuwp.jwt.exceptions.InvalidJwtToken;
import com.liuwp.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: liuwuping
 * Date: 17/5/6
 * Description:
 */
@RestController
public class TokenController {


    @Autowired
    private JwtTokenService tokenService;


    @RequestMapping(value = "/api/auth/token", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String token = tokenService.extractHeadToken(request.getHeader(Constant.JWT_TOKEN_HEADER_PARAM));


        //1.解析token是否有效
        Claims claims = tokenService.parseToken(token);

        RefreshToken rf = null;

        //1.1 检查scopes是否有效
        List<String> scopes = claims.get("scopes", List.class);
        if (!CollectionUtils.isEmpty(scopes)) {
            String scope = scopes.get(0);
            if (!Constant.SCOPE_REFRESH_TOKEN.equals(scope)) {
                throw new InvalidJwtToken();
            } else
                rf = new RefreshToken(claims);

        } else
            throw new InvalidJwtToken();


        //1.2 检查jti是否有效
        String jti = rf.getJti();
//        if (!tokenVerifier.verify(jti)) {
//            throw new InvalidJwtToken();
//        }

        //1.3 查询数据库根据最新的用户和角色生成新的token
        String subject = rf.getSubject();
//        User user = userService.getByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));
//
//        if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getRole().authority()))
//                .collect(Collectors.toList());
        User user = new User();
        user.setUsername("test");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Constant.SCOPE_REFRESH_TOKEN));


        UserContext userContext = UserContext.create(user.getUsername(), authorities);

        return tokenService.createAccessToken(userContext);
    }
}
