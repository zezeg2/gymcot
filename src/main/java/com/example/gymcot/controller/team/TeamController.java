package com.example.gymcot.controller.team;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.team.Team;
import com.example.gymcot.domain.team.TeamRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member/team")
@RequiredArgsConstructor
public class TeamController {

    public Long getSessionId(Authentication authentication){
        return ((PrincipalDetails) authentication.getPrincipal()).getUser().getId();
    }

//    Create
    @PostMapping()
    public void createTeam(Authentication authentication, TeamRequestDto teamRequestDto){

    }
//    Read
//    Update
//    Delete
}
