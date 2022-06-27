package com.example.gymcot.controller.relation;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.relation.RelationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationService;


@RestController
@RequestMapping("/api/v1/member/relation")
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;

    public Long getSessionId(Authentication authentication){
        return ((PrincipalDetails) authentication.getPrincipal()).getUser().getId();
    }

    @PostMapping("/{dtype}")
    public void makeRelation(Authentication authentication, @PathVariable String dtype, @RequestBody RelationRequestDto relationRequestDto){
        relationService.makeRelation(getSessionId(authentication))
    }
}
