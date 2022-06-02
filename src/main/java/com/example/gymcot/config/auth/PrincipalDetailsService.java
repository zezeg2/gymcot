package com.example.gymcot.config.auth;

import com.example.gymcot.config.auth.oauth.provider.FacebookUserInfo;
import com.example.gymcot.config.auth.oauth.provider.GoogleUserInfo;
import com.example.gymcot.config.auth.oauth.provider.NaverUserInfo;
import com.example.gymcot.config.auth.oauth.provider.OAuth2UserInfo;
import com.example.gymcot.domain.member.Member;
import com.example.gymcot.domain.member.Role;
import com.example.gymcot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// security 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행
// session(내부 Authentication(내부 UserDetails(PrincipalDetails(userEntity)))))
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService extends DefaultOAuth2UserService implements UserDetailsService  {


    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByUsername(memberName);
        if (memberEntity!= null) return new PrincipalDetails(memberEntity); //  PrincipalDetails 타입으로 리턴
        return null;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /**
         *  RegistrationId 로 어떤 OAuth로(Google, Facebook) 로그인 했는지 확인 가능.
         *  구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리)
         *  -> AccessToken 요청 -> userRequest 정보 -> loadUser 함수 호출
         *  -> 구글로부터 회원 프로필을 받아준다.
         */

        log.info("userRequest.getClientRegistration : " + userRequest.getClientRegistration().toString());
        log.info("userRequest.getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User  = super.loadUser(userRequest);
        log.info("super.loadUser(userRequest) : " + oAuth2User.getAttributes());


        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")){
            log.info("Google 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            log.info("Facebook 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            log.info("Naver 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String memberName = oAuth2UserInfo.getName();
        String OAuthId = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("getInThere");
        Role role = Role.ROLE_MEMBER;

        Member memberEntity = memberRepository.findByUsername(memberName);

        if(memberEntity == null){
            log.info(provider + " 로그인이 최초입니다. ");
            memberEntity = Member.builder()
                    .OAuthId(OAuthId)
                    .email(email)
                    .username(memberName)
                    .nickName(memberName)
                    .password(password)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(memberEntity);
        }
        //  PrincipalDetails 타입으로 리턴
        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }
}
