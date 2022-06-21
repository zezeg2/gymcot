package com.example.gymcot.controller.user;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.UserRequestDto;
import com.example.gymcot.domain.user.UserResponseDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerApiController extends UserApiController {
    public ManagerApiController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        super(authenticationManager, userRepository, userService);
    }

    @GetMapping
    public UserResponseDto manager(Authentication authentication){
        return userService.getUser(getSessionId(authentication));
    }

    @PostMapping
    public void updateManager(Authentication authentication, @RequestBody @Valid UserRequestDto userDto){
        User user = userService.update(getSessionId(authentication), userDto);
        Authentication updatedAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

    @GetMapping("/members")
    public List<UserResponseDto> memberList(Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return userService.memberList(principal.getUser().getGym().getId());
    }

    @DeleteMapping("delete")
    public void delete(Authentication authentication){
        userRepository.deleteById(getSessionId(authentication));
    }
}
