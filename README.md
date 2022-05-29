# Spring Security 



### SecurityConfig

```java
// Configuration 등록
@Configuration
// 스프링 시큐리티 필터가 스프링 필터체인에 등록된다
@EnableWebSecurity 
// Sucure 어노테이션 활성화, PreAuthorize, PostAutorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) 

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
                .and().formLogin().loginPage("/loginForm") // 로그인 페이지 설정
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 일반 로그인을 진행
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login().loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOAuth2UserService);
    }

}
```

**SecurityConfig.java 권한 설정 방법**

```java
// protected void configure(HttpSecurity http) 함수 내부에 권한 설정법
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
.anyRequest().permitAll()
```

**일반 로그인 설정 방법**

```java
.and().formLogin().loginPage("/loginForm") // 일반 로그인 페이지 설정
.loginProcessingUrl("/login") 
 /**
	* /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 일반 로그인을 진행
	*  login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 메소드가 실행되고 UserDetails 객체 리턴
	*  UserDetailsService를 상속하여 loadUserByUsername 구현하면 커스텀 타입으로 리턴가능
	*  단 커스텀 타입 -> UserDetails를 구현해야함
	*/
.defaultSuccessUrl("/") // 로그인이 요구된(시도된) 웹페이지로 리다이렉트
```

**OAuth 로그인 설정 방법**

```java
.and()
.oauth2Login().loginPage("/loginForm") // OAuth 로그인 페이지 설정
.userInfoEndpoint()  // /oauth2/authorization/**(provider) -> 유저 info 엔드포인트 규칙
.userService(principalOAuth2UserService); 
// 위 엔드포인트로 요청되면 후처리를 위해 특정 서비스로 던짐
// 설정하지 않으면 DefaultOAuth2UserService 서비스로 타입으로 IoC 되어있는 loadUser 메소드가 실행되고 OAuthUser 객체 리턴
// DefaultOAuth2UserService를 상속하여 loadUser 구현하면 커스텀 타입으로 리턴가능
```

**컨트롤러의 함수에 직접 권한 설정 하는 방법**

```java
// 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화 SecurityConfig.java에 설정
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)

// 컨트롤러에 어노테이션 거는 법
@PostAuthorize("hasRole('ROLE_MANAGER')")
@PreAuthorize("hasRole('ROLE_MANAGER')")
@Secured("ROLE_MANAGER")
```

### SecurityContext, Authentication

- 서버 내부에 security 가 관리하는 별도의 session이 존재 → **Security Context**

- **Security Context**에 들어갈 수 있는 객체의 타입은  **Authentication**  객체 뿐!

- Authentication는 현재 접근하는 주체의 정보와 권한을 담는 인터페이스이다.

- SecurityContextHolder를 통해 SecurityContext에 접근, SecurityContext를 통해 Authentication에 접근할 수 있다.

	```java
	public interface Authentication extends Principal, Serializable {
			// 현재 사용자의 권한 목록을 가져옴
	    Collection<? extends GrantedAuthority> getAuthorities();
	
			// credentials(주로 비밀번호)을 가져옴Object getCredentials();
	
	    Object getDetails();
	
			// Principal 객체를 가져옴.
			Object getPrincipal();
	
			// 인증 여부를 가져옴
			boolean isAuthenticated();
	
			// 인증 여부를 설정함
			void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
	}
	```

- session에 존재하는 Authentication은 필요할때 컨트롤러, 서비스에서 DI가능

- Authentication 객체에 들어갈 수 있는 두가지 타입

	- UserDetails → 일반적인 로그인

	- OAuth2User → OAuth 로그인(구글, 페이스북 ..)

		→ 세션 객체가 필요한 컨트롤러/서비스에서 따로 구현을 해야하는것일까?  → NO, 두가지를 모두 구현한  클래스를 이용한다 (PrincipalDetails)

		```java
		@Controller
		public class IndexController {
		
		    @Autowired
		    private UserService userService;
		
		    @GetMapping("/test/login")
		    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){
		
		        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		        System.out.println(principalDetails.getUser());
		        System.out.println("userDetails : " + userDetails.getUser());
		        return "세션정보 확인하기";
		    }
		
		    @GetMapping("/test/oauth/login")
		    public @ResponseBody String oauthLoginTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth){
		
		        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		        System.out.println(oAuth2User.getAttributes());
		        System.out.println("oAuth : " + oAuth.getAttributes());
		        return "OAuth 세션정보 확인하기"; 
		}
		```

	- 회원가입, 로그인시 User 객체를 사용해야하는데 UserDetails, OAuth2Details 타입은 User 오브젝트를 포함하고 있지 않다

	- PrincipalDetails에서  User 객체를 포함시키고 UserDetails, OAuth2Details를 구현하여. 복잡도를 낮추고, Authentication에서 유저 객체를 가질수 있게 한다.

		```java
		@Getter
		public class PrincipalDetails implements UserDetails, OAuth2User {
				
				// User 객체를 가짐
		    private User user; 
		
				// OAuth 서버로부터(Google, Facebook ...) 받는 데이터를 Map에 담음
		    private Map<String, Object> attributes;
		
		    // 일반 로그인시 PrincipalDetailsService에서 사용할 Constructor
		    public PrincipalDetails(User user) {
		        this.user = user;
		    }
		    // OAuth 로그인시 PrincipalOAuthDetailsService에서 사용할 Constructor
		    public PrincipalDetails(User user, Map<String, Object> attributes) {
		        this.user = user;
		        this.attributes = attributes;
		    }
		
		    @Override // 인터페이스 메소드 구현
				...
		    ...
		}
		```

	- **PrincipalDetails 타입을 리턴하기 위해서 커스텀 service를 구현한다**