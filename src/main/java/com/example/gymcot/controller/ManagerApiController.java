package com.example.gymcot.controller;

import com.example.gymcot.domain.gym.GymDto;
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
@RequestMapping("/api/v1/manager")
public class ManagerApiController extends UserApiController {
    public ManagerApiController(UserRepository userRepository, UserService userService) {
        super(userRepository, userService);
    }

    @GetMapping
    public User manager(Authentication authentication){
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping
    public void updateManager(Authentication authentication, UserDto userDto){
        userService.update(getSessionId(authentication), userDto);
    }

    @PostMapping("/enrolGym")
    public void enrollGym(GymDto gymDto){
        userService.enrollGym(gymDto);
    }

}
