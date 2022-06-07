package com.example.gymcot.controller;

import com.example.gymcot.domain.member.User;
import com.example.gymcot.domain.member.UserDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.member.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AdminApiController extends UserApiController {

    public AdminApiController(UserRepository userRepository, UserService userService) {
        super(userRepository, userService);
    }

    @PostMapping("/admin")
    public void updateMember(Authentication authentication, UserDto userDto){
        userService.update(getSessionId(authentication), userDto);
    }

    @GetMapping("/admin")
    public User manager(Authentication authentication){
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping("/admin/cr/{id}/{role}")
    public void changeRole(@PathVariable Long id, String role){
        userService.changeRole(id, role);
    }
}
