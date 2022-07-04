package com.example.gymcot.controller.user;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public abstract class UserApiController {
    protected final AuthenticationManager authenticationManager;
    protected final UserRepository userRepository;
    protected final UserService userService;

    public Long getSessionId(Authentication authentication){
        return ((PrincipalDetails) authentication.getPrincipal()).getUser().getId();
    }
}
