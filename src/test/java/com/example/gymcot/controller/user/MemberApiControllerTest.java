package com.example.gymcot.controller.user;

import com.example.gymcot.domain.user.UserRequestDto;
import com.example.gymcot.service.UserService;
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
                        .cookie(new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzA5MDkxLCJ1c2VybmFtZSI6InplemVnMiJ9.udXOI115z0xAl1T3p2S10UotM3pnX8QJEqeQZSCJ2Mqkzu8mRrkgSqcrt8xtEl-CrI403k23d0LyD6sX4g3SUw"))
                        .cookie(new Cookie("remember-me", "MmhaejZYNGswMGRwaG1OYkpUMHNGQSUzRCUzRDo5JTJGMW5tZXRQJTJGTjkyNGJIOFhNaGpjdyUzRCUzRA")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("updateMember")
    @Test
    void updateMember() throws Exception {
        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzA5MDkxLCJ1c2VybmFtZSI6InplemVnMiJ9.udXOI115z0xAl1T3p2S10UotM3pnX8QJEqeQZSCJ2Mqkzu8mRrkgSqcrt8xtEl-CrI403k23d0LyD6sX4g3SUw"))
                        .cookie(new Cookie("remember-me", "MmhaejZYNGswMGRwaG1OYkpUMHNGQSUzRCUzRDo5JTJGMW5tZXRQJTJGTjkyNGJIOFhNaGpjdyUzRCUzRA"))
                        .content(objectMapper.writeValueAsString(UserRequestDto.builder()
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
                        .cookie(new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzA3MDQzLCJ1c2VybmFtZSI6InplemVnMiJ9.SAE5L3x_aanj55UZ7MEC-MzQT-rU4BO_n1-HC2bIJKK8cTwoV4iamuhWSiKVEOdk8x-m3AFMwcC-9qdwuIwZgg" +
                                ""))
                        .cookie(new Cookie("remember-me", "cmIyd0hlMGFZaFNJTXVxd0kwN1l4USUzRCUzRDpheFRleDVGRTk3azJKaWVuSmI5TjR3JTNEJTNE")))
                .andExpect(status().isOk())
                .andDo(print());
    }
}