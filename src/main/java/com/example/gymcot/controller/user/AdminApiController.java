package com.example.gymcot.controller.user;

import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.UserRequestDto;
import com.example.gymcot.domain.user.UserResponseDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminApiController extends UserApiController {

    public AdminApiController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        super(authenticationManager, userRepository, userService);
    }

    @PutMapping
    public void updateAdmin(Authentication authentication, @RequestBody @Valid UserRequestDto userDto){
        User user = userService.update(getSessionId(authentication), userDto);
        Authentication updatedAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

    @GetMapping
    public UserResponseDto admin(Authentication authentication){
        return userService.getUser(getSessionId(authentication));
    }

    @PostMapping("/cr/{username}/{role}")
    public void changeRole(@PathVariable String username, @PathVariable String role){
        userService.changeRole(username, role);
    }

    @GetMapping("/managers")
    public List<UserResponseDto> managerList(){
        return userService.managerList();
    }

    @DeleteMapping("delete")
    public void delete(Authentication authentication){
        userRepository.deleteById(getSessionId(authentication));
    }
}
