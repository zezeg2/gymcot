# SpringBoot Security Configuration

### SecurityConfig

1. jwt 토큰 발행을 위해UsernamePasswordAuthenticationFilter 커스터마이징
	1. 인증 성공시(로그인 시) 쿠키형태로 jwt 반환
	2. 인증 실패시 AuthenticationFailure로 예외처리 (Not Found User)
2.  jwt 인증방식을 사용하기 위해 BasicAuthenticationFilter 커스터마이징
	1. 인증이나 권한이 필요한 주소에 요청이 있을때 해당 필터를 타게 됨
	2. 매 요청마다 쿠키로부터 jwt토큰을 얻어 인증하는 방식
	3. jwt 토큰 만료되었을 경우 remember-me 토큰 유효할 시 토큰 재발행
	4. 예외 발생시 AuthenticationEntirypoint로 예외처리

SecurityConfig.java

- jwt인증방식으로 사용하므로 세션인증방식 비활성화
- 권한 매핑
- OAuth2 로그인 활성화
- 로그아웃시 인증관련 쿠키 삭제(jwt, remember-me) 등 설정

```java
@Configuration
@EnableWebSecurity//(debug = true)
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
```

JwtAuthenticationFilter -> UsernamePasswordAuthentication Filter 상속

```java
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private RememberMeServices rememberMeServices;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        super(authenticationManager);
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    public RememberMeServices getRememberMeServices() {
        return super.getRememberMeServices();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("-----------------------  JwtAuthenticationFilter  -----------------------");
        try {
            User user;
            ObjectMapper om = new ObjectMapper();
            user = om.readValue(request.getInputStream(), User.class);

            log.info("Input Value : {} ", user.toString());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            Authentication authentication = super.getAuthenticationManager().authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("Login successful : {}", principalDetails.getUser().getUsername());
            return authentication;

        } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
            setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

            try {
                request.setAttribute("exception", ExceptionCode.NOT_FOUND_USER.getCode());
                unsuccessfulAuthentication(request, response, e);
            } catch (IOException | ServletException ex) {
                ex.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Login Request Denied");
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain
            chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetail = (PrincipalDetails) authResult.getPrincipal();

        /* Hash 암호방식 */
        String jwt = genToken(response, principalDetail);
        super.setRememberMeServices(rememberMeServices);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
```

JwtAuthorizationFilter -> BasicAuthenticationFilter 상속

```java
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
            if (!jwt.startsWith("Bearer")) {
                request.setAttribute("exception", ExceptionCode.INVALID_TOKEN.getCode());
                chain.doFilter(request, response);
                return;
            }

            String username = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(jwt.replace(TOKEN_PREFIX, "")).getClaim("username").asString();

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
```

SecurityBeans.java

```java
@Configuration
@RequiredArgsConstructor
public class SecurityBeans {

    private final DataSource dataSource;

    private final PrincipalDetailsService principalDetailsService;

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_MEMBER");
        return roleHierarchy;
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 서버가 응답할때 json을 JS에서 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*"); // 모든 IP에 응답을 허용
        config.addAllowedHeader("*"); // 모든 Header에 응답을 허용
        config.addAllowedMethod("*"); // 모든 Http Method 요청을  허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
    @Bean
    PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try {
            repository.removeUserTokens("1");
        } catch (Exception ex) {
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices("gymcot",
                        principalDetailsService,
                        tokenRepository()
                );
        service.setTokenValiditySeconds(60*60*24*30);
        return service;
    }
}
	
```



### OAuth2 관련 설정

- Security가 SecurityContext에 인증정보를 저장할 객체 UserDatails와 OAuth2 를 통해 인증한 정보를 저장할 수 있도록 OAuth2User를 상속하여 PrincipalDetails 메소드를 구현한 클래스 생성
- 인증과정에서 유저의 정보를 가저올 수 있도록 하는 인터페이스 UserDetails를 구현하고 OAuth2 인증과정에서 필요한 유저정보를 가져올 수 있도록 DecaultOAuth2Service 메소드를 구현한 클래스 생성

(코드생략)
