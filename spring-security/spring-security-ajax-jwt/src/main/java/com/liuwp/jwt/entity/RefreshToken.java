package com.liuwp.jwt.entity;

import io.jsonwebtoken.Claims;

/**
 * Author: liuwuping
 * Date: 17/5/6
 * Description:
 */
public class RefreshToken implements JwtToken {

    private Claims claims;


    public RefreshToken(Claims claims) {
        this.claims = claims;
    }

    @Override
    public String getToken() {
        return null;
    }


    public Claims getClaims() {
        return claims;
    }


    public String getJti() {
        return claims.getId();
    }

    public String getSubject() {
        return claims.getSubject();
    }
}
