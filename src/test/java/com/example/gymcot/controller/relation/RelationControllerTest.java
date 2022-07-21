package com.example.gymcot.controller.relation;

import com.example.gymcot.domain.diary.Exercise;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        cookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtZW1iZXIxIiwiaWQiOjEsImV4cCI6MTY1ODM5MzU3OCwidXNlcm5hbWUiOiJtZW1iZXIxIn0.uYzkjzJfsG9gvNnNZ9C2zmn5S5o6vXoyacYNNID7oR4joKsjFDBHwzrk7IQ2OM9ouWcgow0d686E6R2_IzvTjw")
                , new Cookie("remember-me", "bUNVdkRQVnJmR1ZFYmlObDREaHRkZyUzRCUzRDpQTWM5Nmh6SFolMkYlMkJuZ2FlSWt6UHZIQSUzRCUzRA")};
//        cookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtZW1iZXIyIiwiaWQiOjIsImV4cCI6MTY1ODM5MzYwOSwidXNlcm5hbWUiOiJtZW1iZXIyIn0.NnuWtlDvCmxjawgSg7P3wskOy9edBMlFtK1Wis07Lox4UihauwBQrizFUfvOP2dmRxVKtV52Zxna8OW9GaNoDg")
//                , new Cookie("remember-me", "NWp1NEFDMGJuMCUyRm1JbW5IZlhOZWdnJTNEJTNEOko0Vm0lMkZzR2dVa1hMV29EUnpSZiUyQlRnJTNEJTNE")};
    }


    @Test
    void join() throws Exception {
        for (int i = 1; i <= 20; i++) {
            Map<String, String> input = joinInput("member" + i, "010-2086-9320", "member" + i + "@google.com", "whdqkr003#");

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }


    @Test
    void rememberMeLoginTest() throws Exception {
        Map<String, String> input = loginInput("member2", "whdqkr003#");
        Cookie[] cookies = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .param("remember-me", "true"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().getCookies();

        Arrays.stream(cookies).forEach(c -> log.info("{} : {}", c.getName(), c.getValue()));
    }


    @Test
    void makeFriendRelation() throws Exception {
        mockMvc.perform(post("/api/v1/member/relation/f")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(relationInput(null, "member1")))
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void makeTogetherRelation() throws Exception {
        mockMvc.perform(post("/api/v1/member/relation/t")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(relationInput("go together", "member2")))
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void togetherStart() {
    }

    @Test
    void togetherEnd() {
    }

    @Test
    void approveRequest() throws Exception {
        mockMvc.perform(put("/api/v1/member/relation/friend/approve/member2")
                .cookie(cookies)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void approvedFriendList() throws Exception{
        mockMvc.perform(get("/api/v1/member/relation/friend/approved/list")
                        .cookie(cookies)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void waitingFriendList()throws Exception {
        mockMvc.perform(get("/api/v1/member/relation/friend/waiting/list")
                        .cookie(cookies)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteRelation() throws Exception {
        mockMvc.perform(delete("/api/v1/member/relation/friend/delete/member3")
                .cookie(cookies)).andExpect(status().isOk())
                .andDo(print());

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

    private Map<String, String> relationInput(String title, String toUsername) {
        Map<String, String> input = new HashMap<>();

        input.put("title", title);
        input.put("toUsername", toUsername);
        input.put("startAt", LocalDateTime.now().toString());
        input.put("endAt", LocalDateTime.now().plusHours(2).toString());
        return input;
    }


}