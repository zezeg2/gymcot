package com.example.gymcot.controller;

import com.example.gymcot.domain.user.UserUpdateDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class AdminApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    Cookie[] cookies;

    @BeforeEach
    void setUp() {
        cookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlkIjo1LCJleHAiOjE2NTU3MTA2NjEsInVzZXJuYW1lIjoiYWRtaW4ifQ.VoQumKjcOjIBSCMKHa7YkkQfKcGN1gdV6m27vsP6n59AmiW-H1BgMKOPwc7fT7fwo2KjOyX02P6QHhBhuXGgHA")
                , new Cookie("remember-me", "ZXdacm1WN0VMQ1NVSDdjNnhLb0pUdyUzRCUzRDozVjJGUWd1VnBkeGR1T0VpWHE2Nk93JTNEJTNE")};
    }

    @DisplayName("getAdmin")
    @Test
    void getAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin")
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("getAdminFail")
    @Test
    void getAdminFail() throws Exception {
        mockMvc.perform(get("/api/v1/admin")
                        .cookie(new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzA5MDkxLCJ1c2VybmFtZSI6InplemVnMiJ9.udXOI115z0xAl1T3p2S10UotM3pnX8QJEqeQZSCJ2Mqkzu8mRrkgSqcrt8xtEl-CrI403k23d0LyD6sX4g3SUw"))
                        .cookie(new Cookie("remember-me", "MmhaejZYNGswMGRwaG1OYkpUMHNGQSUzRCUzRDo5JTJGMW5tZXRQJTJGTjkyNGJIOFhNaGpjdyUzRCUzRA")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("updateAdmin")
    @Test
    void updateAdmin() throws Exception {
        mockMvc.perform(post("/api/v1/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookies)
                        .content(objectMapper.writeValueAsString(UserUpdateDto.builder()
                                .password("whdqkr003##")
                                .phone("010-1111-2222")
                                .build()
                        )))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("changeRole")
    @Test
    void changeRole() throws Exception {
        mockMvc.perform(post("/api/v1/admin/cr/manager/manager")
                        .cookie(cookies))
                .andExpect(status().isOk());
    }

}

