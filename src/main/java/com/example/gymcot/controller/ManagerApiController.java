package com.example.gymcot.controller;

import com.example.gymcot.domain.member.User;
import com.example.gymcot.domain.member.UserDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.member.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ManagerApiController extends UserApiController {
    public ManagerApiController(UserRepository userRepository, UserService userService) {
        super(userRepository, userService);
    }

    @GetMapping("/manager")
    public User member(Authentication authentication){
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping("/manager")
    public void updateMember(Authentication authentication, UserDto userDto){
        userService.update(getSessionId(authentication), userDto);
    }
}
