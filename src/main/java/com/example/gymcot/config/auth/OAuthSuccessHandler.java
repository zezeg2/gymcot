package com.example.gymcot.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.example.gymcot.config.JwtProperties.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetail = (PrincipalDetails) authentication.getPrincipal();

        /* Hash 암호방식 */
        String jwtToken = JWT.create()
                .withSubject(principalDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("id", principalDetail.getMember().getId())
                .withClaim("memberName", principalDetail.getMember().getMemberName())
                .sign(Algorithm.HMAC512(SECRET));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwtToken);
        log.info("Authentication HEader : {}", TOKEN_PREFIX + jwtToken);
    }
}
