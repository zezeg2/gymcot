package com.example.gymcot.controller.relation;

import com.example.gymcot.domain.user.User;
import com.example.gymcot.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class RelationControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    Cookie[] cookies;

    @BeforeEach
    void setUp() {
        cookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlkIjo1LCJleHAiOjE2NTU4MDQ0NjUsInVzZXJuYW1lIjoiYWRtaW4ifQ.k8Z5RBMSbrFddyoXuHBok1vRf_YA_4wtQcUDChXpuYNoyjFdhvIeXdHMMNxPc35zeAVwzB8e4knTb6aJjyJiaQ")
                , new Cookie("remember-me", "bWxiU3FYS3hoc0ppdUtHaTF3S1VSZyUzRCUzRDpSVkpUZ2Z0SE0lMkJTVEQ4V29LOWZzJTJCUSUzRCUzRA")};
    }


    @Test
    void join() throws Exception {
        for (int i = 0; i < 100; i++){
            Map<String, String> input = joinInput("member" + i, "010-2086-9320", "member" + i +"@google.com", "whdqkr003#");

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
        }


    @Test
    void rememberMeLoginTest() throws Exception {
        Map<String, String> input = loginInput("manager", "whdqkr003#");
        Cookie[] cookies = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .param("remember-me", "true"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().getCookies();

        Arrays.stream(cookies).forEach(c -> log.info("{} : {}",c.getName() ,c.getValue()));
    }


    @Test
    void makeRelation() {
    }

    @Test
    void togetherStart() {
    }

    @Test
    void togetherEnd() {
    }

    @Test
    void approveRequest() {
    }

    @Test
    void approvedFriendList() {
    }

    @Test
    void waitingFriendList() {
    }

    void createUser(String username, String phone, String email, String password) {
        if (userRepository.findByUsername(username) == null) {
            userRepository.save(User.builder()
                    .username(username)
                    .phone(phone)
                    .email(email)
                    .password(password).build());
        }
    }

    private Map<String, String> joinInput(String username, String phone, String email, String password) {
        Map<String, String> input = new HashMap<>();

        input.put("username", username);
        input.put("phone", phone);
        input.put("email", email);
        input.put("password", password);
        return input;
    }

    private Map<String, String> loginInput(String username, String password) {
        Map<String, String> input = new HashMap<>();

        input.put("username", username);
        input.put("password", password);
        return input;
    }
}