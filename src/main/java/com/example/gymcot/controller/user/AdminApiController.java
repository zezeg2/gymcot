package com.example.gymcot.controller.user;

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
@RequestMapping("/api/v1/admin")
public class AdminApiController extends UserApiController {

    public AdminApiController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        super(authenticationManager, userRepository, userService);
    }

    @PostMapping
    public User updateAdmin(Authentication authentication, @RequestBody @Valid UserUpdateDto userDto){
        User user = userService.update(getSessionId(authentication), userDto);
        Authentication updatedAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
        return user;
    }

    @GetMapping
    public User admin(Authentication authentication){
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping("/cr/{username}/{role}")
    public void changeRole(@PathVariable String username, @PathVariable String role){
        userService.changeRole(username, role);
    }
}
