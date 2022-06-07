package com.example.gymcot;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.member.Member;
import com.example.gymcot.domain.member.Role;
import com.example.gymcot.repository.GymRepository;
import com.example.gymcot.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
public class DataInitializer {

    @Autowired private MemberRepository memberRepository;

    @Autowired private GymRepository gymRepository;


    private  void createMember(String name, String nickName, String phone, Role role, String email, String password, Gym gym) {
        Member member = Member.builder()
                .username(name)
                .nickName(nickName)
                .phone(phone)
                .role(role)
                .email(email)
                .password(password)
                .gym(gym)
                .build();
        memberRepository.save(member);

    }


    private void createGym(String name, boolean approved, String location) {
        Gym gym = Gym.builder()
                .name(name)
                .approved(approved)
                .location(location)
                .build();
        gymRepository.save(gym);
    }


    @DisplayName("init data")
    @Test
    @Transactional
    void initData() {
        createGym("healthGoodHealth", true, "dogkjak-gu");
        createMember("jonghyeon", "jby", "010-2086-9320", Role.ROLE_MEMBER, "whdgus003@gmail.com", "whdqkr003", gymRepository.findById(1L).get());
        Member me = memberRepository.findByUsername("jonghyeon");
        System.out.println(me);
    }



}
