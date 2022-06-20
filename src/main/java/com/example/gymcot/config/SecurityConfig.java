package com.example.gymcot.config;

import com.example.gymcot.config.auth.OAuthSuccessHandler;
import com.example.gymcot.config.auth.PrincipalDetailsService;
import com.example.gymcot.config.auth.filters.JwtAuthenticationFilter;
import com.example.gymcot.config.auth.filters.JwtAuthorizationFilter;
import com.example.gymcot.error.CustomAuthenticationEntryPoint;
import com.example.gymcot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity//(debug = true) // 스프링 시큐리티 필터가 스프링 필터체인에 등록된다
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService principalDetailsService;

    private final UserRepository userRepository;

    private final OAuthSuccessHandler oAuthSuccessHandler;

    private final CorsFilter corsFilter;

    private final PersistentTokenBasedRememberMeServices rememberMeServices;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager(), rememberMeServices);
        JwtAuthorizationFilter authorizationFilter = new JwtAuthorizationFilter(authenticationManager(), userRepository, rememberMeServices);
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .addFilter(corsFilter)

                .addFilter(authenticationFilter)
                .addFilter(authorizationFilter)
                .authorizeRequests(request ->
                        request.mvcMatchers("/", "/css/**", "/scripts/**", "/plugin/**", "/fonts/**", "/v2/**", "/configuration/**", "/swagger*/**", "/webjars/**", "/swagger-resources/**").permitAll()
                                .antMatchers("/api/v1/member/**").access("hasRole('ROLE_MEMBER')")
                                .antMatchers("/api/v1/manager/**").access("hasRole('ROLE_MANAGER')")
                                .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                                .anyRequest().permitAll()
                )

                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()

                .formLogin(login -> login
                        .permitAll()// /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
                        .defaultSuccessUrl("/")
                        .failureUrl("/login-error"))

                .oauth2Login(login ->
                        login.successHandler(oAuthSuccessHandler).userInfoEndpoint()
                                .userService(principalDetailsService))

                .logout()
                .addLogoutHandler((LogoutHandler) rememberMeServices)
                .logoutSuccessUrl("/")
                .deleteCookies("Authorization", "remember-me")

                .and()
                .sessionManagement().disable();


    }
}


