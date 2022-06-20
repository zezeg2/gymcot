package com.example.gymcot.controller;

import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.UserUpdateDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class MemberApiController extends UserApiController {

    private final AuthenticationManager authenticationManager;

    public MemberApiController(UserRepository userRepository, UserService userService, AuthenticationManager authenticationManager) {
        super(userRepository, userService);
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/member")
    public User member(Authentication authentication) {
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping("/member")
    public void updateMember(Authentication authentication, @RequestBody @Valid UserUpdateDto userDto) {
        User user = userService.update(getSessionId(authentication), userDto);
        Authentication updatedAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

    }

    @PostMapping("/member/attend")
    public void changeState(Authentication authentication) {
        userService.toggleState(getSessionId(authentication));
    }

}
