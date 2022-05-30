package com.example.gymcot.service.member;

import com.example.gymcot.domain.member.Member;
import com.example.gymcot.domain.member.MemberRepository;
import com.example.gymcot.domain.member.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void join(Member member){
        member.setRole(Role.MEMBER);
        member.setPassword(encoder.encode(member.getPassword()));
        memberRepository.save(member);
        /*
         회원가입은 잘 됨,
         비밀번호 1234 => 시큐리티로 로그인할 수 없음
         이유는 패스워드가 암호화 안되었기 때문
         */
    }


}
