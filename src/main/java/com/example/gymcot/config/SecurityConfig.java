package com.example.gymcot.config;

import com.example.gymcot.config.auth.PrincipalOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록된다
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // Sucure 어노테이션 활성화, PreAuthorize, PostAut인orize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PrincipalOAuth2UserService principalOAuth2UserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and().formLogin().loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login().loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOAuth2UserService);
        // 구글 로그인이 완료된 뒤의 후처리가 필요,
        /**
         *  1. 코드 받기(인증),
         *  2. 액세스 토큰,
         *  3.사용자프로필 정보를 가져오고,
         *  4. 그 정보를 토대 회원가입을 진행시키기도함,
         *  5.추가정보
          */

    }
    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해줌

}
