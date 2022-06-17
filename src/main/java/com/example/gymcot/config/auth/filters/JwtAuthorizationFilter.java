package com.example.gymcot.config.auth.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.error.ExceptionCode;
import com.example.gymcot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

import static com.example.gymcot.config.JwtProperties.*;

/*
 시큐리티 필터중 BasicAuthenticationFilter 라는 것이 있음
 권한이나 인증이 필요한 특정 주소를 요청했을 때 이 필터를 타게 되어있음
 만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 타지 않음
 */


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private UserRepository userRepository;

    private RememberMeServices rememberMeServices;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RememberMeServices rememberMeServices) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        super.onUnsuccessfulAuthentication(request, response, failed);
    }

    @Override
    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        super.setRememberMeServices(rememberMeServices);
    }

    /* 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게됨. */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("인증이나 권한이 필요한 주소가 요청이 됨 ");

        String jwt = null;
        try {
            Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> {
                return c.getName().equals("Authorization");
            }).findAny().get();

            jwt = URLDecoder.decode(cookie.getValue(), "UTF-8");
        } catch (NullPointerException | NoSuchElementException e) {
            request.setAttribute("exception", ExceptionCode.NONE_TOKEN.getCode());
            chain.doFilter(request, response);
        }

        try {
            String username = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(jwt).getClaim("username").asString();
            if (!jwt.startsWith("Bearer")) {
                request.setAttribute("exception", ExceptionCode.INVALID_TOKEN.getCode());
                chain.doFilter(request, response);
                return;
            }

            jwt.replace(TOKEN_PREFIX, "");

            /* 서명이 정상적으로 되었을 때 */
            if (username != null) {
                User userEntity = userRepository.findByUsername(username);
                PrincipalDetails principalDetail = new PrincipalDetails(userEntity);

                /* JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다 */
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetail, null, principalDetail.getAuthorities());
                setAuthority(authentication);

                chain.doFilter(request, response);
            }

        } catch (Exception fe) {

            if (fe.getClass() == TokenExpiredException.class) {
                try {
                    Authentication authentication = rememberMeServices.autoLogin(request, response);
                    PrincipalDetails principalDetail = (PrincipalDetails) authentication.getPrincipal();
                    genToken(response, principalDetail);
                    setAuthority(authentication);
                    chain.doFilter(request, response);


                } catch (NullPointerException se) {
                    SecurityContextHolder.clearContext();
                    request.setAttribute("exception", ExceptionCode.EXPIRED_TOKEN.getCode());
                    chain.doFilter(request, response);
                }

            } else if (fe.getClass() == JWTVerificationException.class) {
                request.setAttribute("exception", ExceptionCode.INVALID_TOKEN.getCode());
            }
        }
    }

    private void setAuthority(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    }
}