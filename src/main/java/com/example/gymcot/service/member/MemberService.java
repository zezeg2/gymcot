package com.example.gymcot.service.member;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.member.Member;
import com.example.gymcot.domain.member.MemberCreateDto;
import com.example.gymcot.domain.member.Role;
import com.example.gymcot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;


    public void join(MemberCreateDto memberCreateDto){
        Member member = memberCreateDto.toEntity();
        member.setRole(Role.ROLE_MEMBER);
        member.setPassword(encoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    public void update(Long id, String nickName , String password, String phone){
        Member findMember = memberRepository.findById(id).get();
        if (nickName!=null) {
            validateDuplicateNickname(findMember);
            findMember.setNickName(nickName);
        }
        if (password!=null) {
            findMember.setPassword(encoder.encode(password));
        }
        if (phone!=null) findMember.setPhone(phone);
    }

    public void setGym(Gym gym, Long id){
        Member findMember = memberRepository.findById(id).get();
        findMember.setGym(gym);
        memberRepository.save(findMember);
    }


    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByUsername(member.getUsername());
        if (findMember!=null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    private void validateDuplicateNickname(Member member){
        Member findMember = memberRepository.findByNickName(member.getNickName());
        if (findMember!=null){
            throw new IllegalStateException("이미 존재하는 닉네임 입니다.");
        }
    }


}
