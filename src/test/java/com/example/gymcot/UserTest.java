package com.example.gymcot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;


    @DisplayName("memberNormalJoin")
    @Test
    void memberNormalJoin() throws Exception {

        Map<String, String> input = new HashMap<>();

        // body에 json 형식으로 회원의 데이터를 넣기 위해서 Map을 이용한다.
        input.put("memberName", "test2");
        input.put("nickName", "test2");
        input.put("phone", "010-2086-9320");
        input.put("email", "test2@google.com");
        input.put("password", "test2_password");

        mockMvc.perform(post("/api/v1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        //json 형식으로 데이터를 보낸다고 명시
                        .content(objectMapper.writeValueAsString(input)))
                //Map으로 만든 input을 json형식의 String으로 만들기 위해 objectMapper를 사용
                .andExpect(status().isOk())
                //Http 200을 기대
                .andDo(print());
        //화면에 결과를 출력
    }

    @DisplayName("loginTest")
    @Test
    void loginTest() throws Exception {

        Map<String, String> input = new HashMap<>();

        // body에 json 형식으로 회원의 데이터를 넣기 위해서 Map을 이용한다.
        input.put("memberName", "test2");
        input.put("password", "test2_password");


        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                //Http 200을 기대
                .andDo(print());

    }
}


