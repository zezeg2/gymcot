package com.example.gymcot.controller;

import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.UserDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MemberApiController extends UserApiController {

    public MemberApiController(UserRepository userRepository, UserService userService) {
        super(userRepository, userService);
    }

    @GetMapping("/member")
    public User member(Authentication authentication) {
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping("/member")
    public void updateMember(Authentication authentication, UserDto userDto) {
        userService.update(getSessionId(authentication), userDto);
    }

    @PostMapping("attend")
    public void changeState(Authentication authentication) {
        userService.toggleState(getSessionId(authentication));
    }

}
