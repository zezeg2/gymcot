package com.example.gymcot;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.Role;
import com.example.gymcot.repository.GymRepository;
import com.example.gymcot.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
public class DataInitializer {

    @Autowired private UserRepository userRepository;

    @Autowired private GymRepository gymRepository;


    private  void createMember(String name, String nickName, String phone, Role role, String email, String password, Gym gym) {
        User user = User.builder()
                .username(name)
                .nickName(nickName)
                .phone(phone)
                .role(role)
                .email(email)
                .password(password)
                .gym(gym)
                .build();
        userRepository.save(user);

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
        User me = userRepository.findByUsername("jonghyeon");
        System.out.println(me);
    }



}
