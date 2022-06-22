package com.example.gymcot.controller.gym;

import com.example.gymcot.domain.gym.GymDto;
import com.example.gymcot.domain.gym.GymRequestDto;
import com.example.gymcot.repository.GymRepository;
import com.example.gymcot.repository.UserRepository;
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

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
class GymControllerTest {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    private Cookie[] memberCookies;
    private Cookie[] managerCookies;
    private Cookie[] adminCookies;

    @BeforeEach
    void setUp() {
        memberCookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzkzMDA5LCJ1c2VybmFtZSI6InplemVnMiJ9.In2jLqOg8Di34N0yV5vz9inznc1eCp9SK2B_ZZf3WEx4t_yWrvFEh2NadcbBKP4e8FTGBKCDmVnkTsZPzuHadQ")
                , new Cookie("remember-me", "Vks3a0VOOUc0T2p3RTZLQzFpVzJTdyUzRCUzRDo2OFBSekF5Sjl4bTMwWlh1allhSmd3JTNEJTNE")};
        managerCookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5hZ2VyIiwiaWQiOjYsImV4cCI6MTY1NTgwNDU1MywidXNlcm5hbWUiOiJtYW5hZ2VyIn0.WE8BKHBISW8UhgCXZm0RfZsIeVQk9z_SlDmvI1Z4ExzY3R4Vbc4FkJrjfgPMKWqkMEAIL50YV9kR33MwVIlS_Q")
                , new Cookie("remember-me", "bEJlU014V24waTBrMHZxTUxvcE5tUSUzRCUzRDpZcU9GM0xENUh6YjhYUHpYZFViSXh3JTNEJTNE")};
        adminCookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlkIjo1LCJleHAiOjE2NTU4MDQ0NjUsInVzZXJuYW1lIjoiYWRtaW4ifQ.k8Z5RBMSbrFddyoXuHBok1vRf_YA_4wtQcUDChXpuYNoyjFdhvIeXdHMMNxPc35zeAVwzB8e4knTb6aJjyJiaQ")
                , new Cookie("remember-me", "bWxiU3FYS3hoc0ppdUtHaTF3S1VSZyUzRCUzRDpSVkpUZ2Z0SE0lMkJTVEQ4V29LOWZzJTJCUSUzRCUzRA")};
    }

    @DisplayName("searchGym")
    @Test
    void searchGym() throws Exception {
        mockMvc.perform(get("/api/v1/gym")
                        .param("query", "서초동 헬스장"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("enrollTest")
    @Test
    void enrollTest() throws Exception {
        GymRequestDto gymRequestDto = GymRequestDto.builder()
                .title("바디스펙트럼5")
                .link("https://www.bodyspectrum.co.kr/")
                .category("스포츠,오락>스포츠시설")
                .description("")
                .telephone("")
                .category("스포츠,오락>스포츠시설")
                .address("서울특별시 서초구 서초동 1328-11 도씨에빛2 건물 311,314호")
                .roadAddress("서울특별시 서초구 강남대로 359 도씨에빛2 건물 311,314호")
                .mapx(314303)
                .mapy(544075)
                .build();
        mockMvc.perform(post("/api/v1/gym/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(managerCookies)
                        .content(objectMapper.writeValueAsString(gymRequestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("approveTest")
    @Test
    void approveTest() throws Exception {
        mockMvc.perform(post("/api/v1/gym/approve/5")
                        .cookie(adminCookies))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("preEnrolledList")
    @Test
    void preEnrolledList() throws Exception {
        mockMvc.perform(get("/api/v1/gym/pre-enrolled")
                        .cookie(adminCookies))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @DisplayName("EnrolledList")
    @Test
    void enrolled() throws Exception {
        mockMvc.perform(get("/api/v1/gym/enrolled")
                        .cookie(adminCookies))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("myGym")
    @Test
    void myGym() throws Exception {
        mockMvc.perform(get("/api/v1/gym/my-gym")
                        .cookie(managerCookies))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("search")
    @Test
    void searchedList() throws Exception {
        mockMvc.perform(get("/api/v1/gym/search")
                        .cookie(memberCookies)
                        .param("title", "바디")
                        .param("roadAddress", "서울")
                        )
                .andExpect(status().isOk())
                .andDo(print());
    }

}