package com.example.gymcot.controller;

import com.example.gymcot.domain.gym.GymDto;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.UserUpdateDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public void updateManager(Authentication authentication, @RequestBody @Valid UserUpdateDto userDto){
        userService.update(getSessionId(authentication), userDto);
    }

    @PostMapping("/enrolGym")
    public void enrollGym(GymDto gymDto){
        userService.enrollGym(gymDto);
    }

}
