package com.example.gymcot.controller.gym;

import com.example.gymcot.domain.gym.GymDto;
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
        memberCookies = new Cookie[]{ new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZXplZzIiLCJpZCI6NCwiZXhwIjoxNjU1NzkzMDA5LCJ1c2VybmFtZSI6InplemVnMiJ9.In2jLqOg8Di34N0yV5vz9inznc1eCp9SK2B_ZZf3WEx4t_yWrvFEh2NadcbBKP4e8FTGBKCDmVnkTsZPzuHadQ")
                , new Cookie("remember-me", "Vks3a0VOOUc0T2p3RTZLQzFpVzJTdyUzRCUzRDo2OFBSekF5Sjl4bTMwWlh1allhSmd3JTNEJTNE")};
        managerCookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5hZ2VyIiwiaWQiOjYsImV4cCI6MTY1NTc5NDY5NCwidXNlcm5hbWUiOiJtYW5hZ2VyIn0.Lq-4efcbMpgQIkvZGyeFLIOxH_-vgUszYM_CdeW6LybckbCLHHpCiuMfl4ApnPtv9sXWLf70oq_Ue584tRYyFg")
                , new Cookie("remember-me", "eGFPbnZZc1lyZHdaREpzc1YydVVIQSUzRCUzRDoxQ3ElMkJCaFF2emElMkJTVUpoU2FFS0lQUSUzRCUzRA")};
        adminCookies = new Cookie[]{new Cookie("Authorization", "Bearer+eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlkIjo1LCJleHAiOjE2NTU3OTQ4MjksInVzZXJuYW1lIjoiYWRtaW4ifQ.uLfpS5ZJMlD4LLgxCMg42kOtkdpW-eFf4LG1Y0Pyo8MeHrrUyga2n-06W8bQG3Cd5CDp6_CEK7-ZDMtWkNscDQ")
                , new Cookie("remember-me", "cU1IenFYSnAlMkI1SVhqcDNYOXIlMkJuM0ElM0QlM0Q6a1BkYWtJNW93MUxZUVlZRmlBdVMlMkJ3JTNEJTNE")};
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
        GymDto gymDto = GymDto.builder()
                .title("바디스펙트럼3")
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
        mockMvc.perform(post("/api/v1/gym")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(managerCookies)
                        .content(objectMapper.writeValueAsString(gymDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("approveTest")
    @Test
    void approveTest() throws Exception {
        mockMvc.perform(post("/api/v1/gym/approve/4")
                .cookie(adminCookies))
                .andExpect(status().isOk())
                .andDo(print());
    }

}