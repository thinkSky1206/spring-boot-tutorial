package com.liuwp.jwt;

import com.liuwp.config.JwtSettings;
import com.liuwp.jwt.entity.UserContext;
import com.liuwp.jwt.entity.AccessToken;
import com.liuwp.jwt.entity.JwtToken;
import com.liuwp.jwt.exceptions.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Author:liuwp
 * Date: 2017/5/5
 * Description:
 */
@Component
public class JwtTokenService {

//    private static final Integer tokenExpirationTime = 15;
//
//    private static final Integer refreshTokenExpTime = 60;
//
//    private static final String tokenIssuer = "liuwp.com";
//
//
//    private static final String tokenSigningKey = "xm8EV6Hy5RMFK4EEACIDAwQus";


    @Autowired
    private JwtSettings settings;

    public AccessToken createAccessToken(UserContext userContext) {
        if (StringUtils.isEmpty(userContext.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
            throw new IllegalArgumentException("User doesn't have any privileges");

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        List<String> scopes = new ArrayList<>();
        for (GrantedAuthority authority : userContext.getAuthorities()) {
            scopes.add("role_" + authority.getAuthority());
        }
        claims.put("scopes", scopes);

        LocalDateTime currentTime = LocalDateTime.now();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(settings.getTokenExpirationTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessToken(token, claims);
    }

    public JwtToken createRefreshToken(UserContext userContext) {
        if (StringUtils.isEmpty(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", Arrays.asList(Constant.SCOPE_REFRESH_TOKEN));

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(settings.getRefreshTokenExpTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessToken(token, claims);
    }


    public Claims parseToken(String token) {

        try {
            return Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token).getBody();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
//            logger.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
//            logger.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException("JWT Token expired");
        }


    }

    public String extractHeadToken(String header) {
        if (StringUtils.isEmpty(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if (header.length() < Constant.HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }

        return header.substring(Constant.HEADER_PREFIX.length(), header.length());

    }


}
