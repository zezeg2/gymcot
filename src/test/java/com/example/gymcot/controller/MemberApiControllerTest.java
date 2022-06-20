package com.example.gymcot.controller;

import com.example.gymcot.domain.user.UserUpdateDto;
import com.example.gymcot.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("getMember")
    @Test
    void getMember() throws Exception {
        mockMvc.perform(get("/api/v1/member")
                        .cookie(new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzA1MDU1LCJ1c2VybmFtZSI6InplemVnMiJ9.68fAQqS-pROC2c72Srf3yJNh9VNxe1xstdNb7okwovbZDDCSb9zarya0yDwv77R4--ldGx9kSHsPRwQ2PALZ7Q")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("updateMember")
    @Test
    void updateMember() throws Exception {
        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzA1MDU1LCJ1c2VybmFtZSI6InplemVnMiJ9.68fAQqS-pROC2c72Srf3yJNh9VNxe1xstdNb7okwovbZDDCSb9zarya0yDwv77R4--ldGx9kSHsPRwQ2PALZ7Q"))
                        .cookie(new Cookie("remember-me", "WUpmVkdkaEJ2bFJXZzFvdkV2WjRhUSUzRCUzRDoxemFFZGtqQTJBVnVFSkF6QyUyQjNXZnclM0QlM0Q"))
                        .content(objectMapper.writeValueAsString(UserUpdateDto.builder()
                                .username("zezeg2")
                                .password("whdqkr003#")
                                .phone("010-1111-2222")
                                .build()
                        )))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("changeStatus")
    @Test
    void changeStatus() throws Exception {
        mockMvc.perform(post("/api/v1/member/attend")
                        .cookie(new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzA1MDU1LCJ1c2VybmFtZSI6InplemVnMiJ9.68fAQqS-pROC2c72Srf3yJNh9VNxe1xstdNb7okwovbZDDCSb9zarya0yDwv77R4--ldGx9kSHsPRwQ2PALZ7Q"))
                        .cookie(new Cookie("remember-me", "WUpmVkdkaEJ2bFJXZzFvdkV2WjRhUSUzRCUzRDoxemFFZGtqQTJBVnVFSkF6QyUyQjNXZnclM0QlM0Q")))
                .andExpect(status().isOk())
                .andDo(print());
    }
}