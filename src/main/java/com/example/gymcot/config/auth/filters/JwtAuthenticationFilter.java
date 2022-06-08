package com.example.gymcot.config.auth.filters;


import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.member.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.gymcot.config.JwtProperties.genToken;

/*
  스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
  /login 요청에서 username, password 전송하면(post)
  UsernamePasswordAuthenticationFilter 동작
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

//    private final PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("-----------------------  JwtAuthenticationFilter  -----------------------");

        try {
            User user;
            if (request.getContentType().equals("application/x-www-form-urlencoded")){
                String username = obtainUsername(request);
                username = (username != null) ? username : "";
                username = username.trim();
                String password = obtainPassword(request);
                password = (password != null) ? password : "";
                user = User.builder()
                        .username(username)
                        .password(password)
                        .build();
            } else{
                ObjectMapper om = new ObjectMapper();
                user = om.readValue(request.getInputStream(), User.class);
            }
            log.info("Input Value : {} ", user);

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("Login successful : {}", principalDetails.getUser().getUsername());

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetail = (PrincipalDetails) authResult.getPrincipal();

        /* Hash 암호방식 */
        String jwt = genToken(response, principalDetail);
        log.info("Authentication Header : {}", jwt);
//        this.setRememberMeServices(persistentTokenBasedRememberMeServices);
        super.successfulAuthentication(request, response, chain, authResult);
    }

}
