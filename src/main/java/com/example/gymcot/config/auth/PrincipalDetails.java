package com.example.gymcot.config.auth;

import com.example.gymcot.domain.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
 *  로그인 진행이 완료되면 시큐리티 sesseion 을 만들어 준다(Security ContextHolder)
 *  오브젝트 => Authentication 타입 객체
 *  Authentication 안에 User 정보가 있어야됨
 *  User 오브젝트 타입 => UserDetails 타입 객체
 *  Security Session => Authentication => UserDetails(PrincipalDetails)
 */

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(Member member) {
        this.member = member;
    }
    //OAuth 로그인
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    // 해당 유저의 권한을 리턴하는곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().getKey();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //1년동안 로그인 안하면휴면계정으로 설정할
    //현재시간 - 로그인시간 => 1년초과하면 return false
    @Override
    public boolean isEnabled() {
        return true;
    }

    // OAuth2User Method Overriding
    // {sub=105292754991439484613, name=박종현, given_name=종현, family_name=박, picture=https://lh3.googleusercontent.com/a/AATXAJwrXHSA8WuPnDQGnjQDPR7lYOySRqyafoevfbVq=s96-c, email=whdgus003@gmail.com, email_verified=true, locale=ko}
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getUsername();
    }

}


