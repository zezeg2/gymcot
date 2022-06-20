package com.example.gymcot.controller;

import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.UserUpdateDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminApiController extends UserApiController {

    public AdminApiController(UserRepository userRepository, UserService userService) {
        super(userRepository, userService);
    }

    @PostMapping
    public void updateAdmin(Authentication authentication, @RequestBody @Valid UserUpdateDto userDto){
        userService.update(getSessionId(authentication), userDto);
    }

    @GetMapping
    public User admin(Authentication authentication){
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping("/cr/{id}/{role}")
    public void changeRole(@PathVariable Long id, String role){
        userService.changeRole(id, role);
    }
}
