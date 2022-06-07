package com.example.gymcot.controller;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.member.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public abstract class UserApiController {
    protected final UserRepository userRepository;
    protected final UserService userService;

    public Long getSessionId(Authentication authentication){
        return ((PrincipalDetails) authentication.getPrincipal()).getUser().getId();
    }
}
