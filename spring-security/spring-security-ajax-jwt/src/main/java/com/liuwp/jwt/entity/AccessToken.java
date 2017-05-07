package com.liuwp.jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.jsonwebtoken.Claims;

/**
 * Raw representation of JWT Token.
 *
 * @author vladimir.stankovic
 *         <p>
 *         May 31, 2016
 */
public final class AccessToken implements JwtToken {
    private String rawToken;
    @JsonIgnore
    private Claims claims;

    public AccessToken(String token, Claims claims) {
        this.rawToken = token;
        this.claims = claims;
    }

    public String getToken() {
        return this.rawToken;
    }

    public Claims getClaims() {
        return claims;
    }
}
