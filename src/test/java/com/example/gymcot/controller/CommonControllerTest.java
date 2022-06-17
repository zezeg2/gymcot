package com.example.gymcot.controller;

import com.example.gymcot.domain.user.User;
import com.example.gymcot.error.ExceptionCode;
import com.example.gymcot.error.ExceptionPayload;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    private void createUser(String username, String phone, String email, String password) {
        userRepository.save(User.builder()
                .username(username)
                .phone(phone)
                .email(email)
                .password(password).build());
    }

    private Map<String, String> inputValue(String username, String phone, String email, String password) {
        Map<String, String> input = new HashMap<>();

        input.put("username", username);
        input.put("phone", phone);
        input.put("email", email);
        input.put("password", password);
        return input;
    }

    @BeforeEach
    void setUp() {
        createUser("test1", "010-2086-9320", "zezeg2@gmail.com", "whdgus003#");
        createUser("test2", "010-2086-9320", "zezeg2@gmail.com", "whdgus003#");
        createUser("test3", "010-2086-9320", "zezeg2@gmail.com", "whdgus003#");
    }

    @Test
    @DisplayName("index page")
    void index() throws Exception {

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("index"))
                .andDo(print());
    }

    @Test
    void joinSuccess() throws Exception {
        Map<String, String> input = inputValue("test1", "010-2086-9320", "zezeg2@google.com", "whdqkr003#");

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("duplicatedUsername")
    @Test
    void duplicatedUsername() throws Exception {
        Map<String, String> input = inputValue("test1", "010-2086-9320", "test@google.com", "whdqkr003#");

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(content().json(objectMapper.writeValueAsString(new ExceptionPayload(new IllegalArgumentException("이미 존재하는 닉네임 입니다."), ExceptionCode.INVALID_INPUT_VALUE))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("ExceededAccount")
    @Test
    void ExceededAccount() throws Exception {
        Map<String, String> input = inputValue("test4", "010-2086-9320", "zezeg2@gmail.com", "whdqkr003#");

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(content().json(objectMapper.writeValueAsString(new ExceptionPayload(new IllegalArgumentException("같은 이메일로는 최대 3개의 계정까지만 허용됩니다"), ExceptionCode.INVALID_INPUT_VALUE))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("passwordPatternTest")
    @Test
    void passwordPatternTest() throws Exception {
        Map<String, String> input = inputValue("test4", "010-2086-9320", "test@google.com", "wromg-pw");

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(content().json("{\n" +
                        "  \"code\": \"2000\",\n" +
                        "  \"message\": \"Invalid Input Value\",\n" +
                        "  \"detail\": {\n" +
                        "    \"field\": \"password\",\n" +
                        "    \"reason\": \"[password](은)는 비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.\"\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

