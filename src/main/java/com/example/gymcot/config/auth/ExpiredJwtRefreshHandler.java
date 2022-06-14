package com.example.gymcot.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.gymcot.config.JwtProperties.genToken;

@Slf4j
public class ExpiredJwtRefreshHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

            if (response.getHeader("jwt-expired").equals("true")) {
                PrincipalDetails principalDetail = (PrincipalDetails) authentication.getPrincipal();
                /* Hash 암호방식 */
                String jwt = genToken(response, principalDetail);
                log.info("new JwtToken : {}", jwt);
            }

    }
}
