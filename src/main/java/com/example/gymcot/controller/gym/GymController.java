package com.example.gymcot.controller.gym;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.gym.GymDto;
import com.example.gymcot.domain.user.Role;
import com.example.gymcot.repository.GymRepository;
import com.example.gymcot.service.user.gym.GymService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/gym")
@Slf4j
public class GymController {


    private final String clientId = "nNwygSAZdGMRxEvxUKfH";
    private final String clientSecret = "hArczFPTjQ";

    private final GymService gymService;

    private final GymRepository gymRepository;

    private final ObjectMapper objectMapper;

    public GymController(GymService gymService, GymRepository gymRepository, ObjectMapper objectMapper) {
        this.gymService = gymService;
        this.gymRepository = gymRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<GymDto> searchGym(@RequestParam String query) {
        String encoded = null;
        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", query)
                .queryParam("display", 10)
                .encode().build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret).build();

        ResponseEntity<Object> response = restTemplate.exchange(req, Object.class);

        List<GymDto> gymDtoList = new ArrayList<>();

        Map<String, Object> result = (Map<String, Object>) response.getBody();
        ArrayList<Object> parsed = (ArrayList<Object>) result.get("items");
        parsed.forEach(o -> {
            GymDto gymDto = objectMapper.convertValue(o, GymDto.class);
            gymDtoList.add(gymDto);
        });
        return gymDtoList;
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/pre-enrolled")
    public List<GymDto> preEnrolledGymList(){
        return gymService.preEnrolledGymList();
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/enrolled")
    public List<GymDto> EnrolledGymList(){
        return gymService.EnrolledGymList();
    }

    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @PostMapping
    public void enroll(Authentication authentication, @RequestBody GymDto gymDto){
        gymService.enroll(getSessionId(authentication), gymDto);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/approve/{gymId}")
    public void approve(@PathVariable Long gymId){
        gymService.approve(gymId);
    }

    public Long getSessionId(Authentication authentication){
        return ((PrincipalDetails) authentication.getPrincipal()).getUser().getId();
    }
}
