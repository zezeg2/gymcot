package com.example.gymcot.controller.relation;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.relation.RelationRequestDto;
import com.example.gymcot.domain.relation.RelationResponseDto;
import com.example.gymcot.service.relation.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        relationRequestDto.setDtype(dtype);
        relationService.makeRelation(getSessionId(authentication), relationRequestDto);
    }

    @PutMapping("/together-start/{id}")
    public void togetherStart(@PathVariable Long id){
        relationService.setStartAt(id);
    }

    @PutMapping("/together-end/{id}")
    public void togetherEnd(@PathVariable Long id){
        relationService.setEndAt(id);
    }

    @PutMapping("/firend/approve{username}")
    public void approveRequest(Authentication authentication, @PathVariable String username){
        relationService.approveRequest(getSessionId(authentication), username);
    }

    @GetMapping("/firend/approve/list")
    public List<RelationResponseDto> approvedFriendList(Authentication authentication){
        return relationService.getApprovedList(getSessionId(authentication));
    }
    @GetMapping("/firend/waiting/list")
    public List<RelationResponseDto> waitingFriendList(Authentication authentication){
        return relationService.getWatingList(getSessionId(authentication));

    }
}
