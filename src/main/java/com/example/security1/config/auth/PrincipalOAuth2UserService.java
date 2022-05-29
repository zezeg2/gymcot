package com.example.security1.config.auth;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.config.auth.oauth.provider.FacebookUserInfo;
import com.example.security1.config.auth.oauth.provider.GoogleUserInfo;
import com.example.security1.config.auth.oauth.provider.NaverUserInfo;
import com.example.security1.config.auth.oauth.provider.OAuth2UserInfo;
import com.example.security1.domain.member.Member;
import com.example.security1.domain.member.MemberRepository;
import com.example.security1.domain.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;


    /**
     * 구글로부터 받은 userRequest 데이터에 를 후처리 하는 메서드
     * 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.(Controller에서 이용)
     */
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
        String name = oAuth2UserInfo.getName();
        String OAuthId = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("getInThere");
        Role role = Role.MEMBER;

        Member memberEntity = memberRepository.findByMemberName(OAuthId);

        if(memberEntity == null){
            log.info(provider + " 로그인이 최초입니다. ");
            memberEntity = Member.builder()
                    .OAuthId(OAuthId)
                    .email(email)
                    .memberName(name)
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
