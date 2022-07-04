package com.example.gymcot.controller.user;

import com.example.gymcot.domain.user.User;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class CommonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


//    private static UserRepository userRepository;

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

    @BeforeEach
    void setUp() {
        createUser("test1", "010-2086-9320", "zezeg2@gmail.com", "whdgus003#");
        createUser("test2", "010-2086-9320", "zezeg2@gmail.com", "whdgus003#");
        createUser("test3", "010-2086-9320", "zezeg2@gmail.com", "whdgus003#");
    }

    @Test
    @Order(1)
    @DisplayName("index page")
    void index() throws Exception {

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("index"))
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("joinSuccess")
    void joinSuccess() throws Exception {
        Map<String, String> input = joinInput("manager", "010-2086-9320", "manager@google.com", "whdqkr003#");

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("duplicatedUsername")
    @Order(3)
    @Test
    void duplicatedUsername() throws Exception {
        Map<String, String> input = joinInput("test1", "010-2086-9320", "test@google.com", "whdqkr003#");

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
//                .andExpect(content().json(objectMapper.writeValueAsString(new ExceptionPayload(new IllegalArgumentException("이미 존재하는 닉네임 입니다."), ExceptionCode.INVALID_INPUT_VALUE))))
                .andExpect(content().json("{\n" +
                        "  \"code\": \"2000\",\n" +
                        "  \"message\": \"Invalid Input Value\",\n" +
                        "  \"detail\": {\n" +
                        "    \"field\": null,\n" +
                        "    \"reason\": \"이미 존재하는 닉네임 입니다.\"\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("ExceededAccount")
    @Order(4)
    @Test
    void ExceededAccount() throws Exception {
        Map<String, String> input = joinInput("test4", "010-2086-9320", "zezeg2@gmail.com", "whdqkr003#");

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
//                .andExpect(content().json(objectMapper.writeValueAsString(new ExceptionPayload(new IllegalArgumentException("같은 이메일로는 최대 3개의 계정까지만 허용됩니다"), ExceptionCode.INVALID_INPUT_VALUE))))
                .andExpect(content().json("{\n" +
                        "  \"code\": \"2000\",\n" +
                        "  \"message\": \"Invalid Input Value\",\n" +
                        "  \"detail\": {\n" +
                        "    \"field\": null,\n" +
                        "    \"reason\": \"같은 이메일로는 최대 3개의 계정까지만 허용됩니다\"\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("passwordPatternTest")
    @Order(5)
    @Test
    void passwordPatternTest() throws Exception {
        Map<String, String> input = joinInput("test4", "010-2086-9320", "test@google.com", "wromg-pw");

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

    @DisplayName("loginTest")
    @Order(6)
    @Test
    void loginTest() throws Exception {
        Map<String, String> input = loginInput("zezeg2", "whdqkr003#");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().is3xxRedirection());
    }
    @DisplayName("rememberMeLoginTest")
    @Order(6)
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
}

