package com.example.gymcot.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.gymcot.config.auth.PrincipalDetails;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public interface JwtProperties {

    String SECRET = "jby";
    //    Integer EXPIRATION_TIME = 1000 * 60 * 30;
    Integer EXPIRATION_TIME = 10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    static String genToken(HttpServletResponse response, PrincipalDetails principalDetail) {
        String jwtToken = JWT.create()
                .withSubject(principalDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("id", principalDetail.getMember().getId())
                .withClaim("memberName", principalDetail.getMember().getUsername())
                .sign(Algorithm.HMAC512(SECRET));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwtToken);
        return TOKEN_PREFIX + jwtToken;
    }
}

