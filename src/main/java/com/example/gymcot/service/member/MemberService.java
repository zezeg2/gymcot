package com.example.gymcot.service.member;

import com.example.gymcot.domain.member.Member;
import com.example.gymcot.domain.member.MemberCreateDto;
import com.example.gymcot.domain.member.Role;
import com.example.gymcot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;


    public void join(MemberCreateDto memberCreateDto){
        Member member = memberCreateDto.toEntity();
        member.setRole(Role.ROLE_MEMBER);
        member.setPassword(encoder.encode(member.getPassword()));
        memberRepository.save(member);
        /*
         회원가입은 잘 됨,
         비밀번호 1234 => 시큐리티로 로그인할 수 없음
         이유는 패스워드가 암호화 안되었기 때문
         */
    }

    public void update(Long id, String nickName , String password, String phone){
        Member findMember = memberRepository.findById(id).get();
        if (nickName!=null) findMember.setNickName(nickName);
        if (password!=null) {
            findMember.setPassword(encoder.encode(password));
        }
        if (phone!=null) findMember.setPhone(phone);



    }


}
