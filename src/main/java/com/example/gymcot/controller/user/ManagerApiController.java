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
@RequestMapping("/api/v1/manager")
public class ManagerApiController extends UserApiController {
    public ManagerApiController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        super(authenticationManager, userRepository, userService);
    }

    @GetMapping
    public UserResponseDto manager(Authentication authentication){
        return userService.getUser(getSessionId(authentication));
    }

    @PutMapping
    public void updateManager(Authentication authentication, @RequestBody @Valid UserRequestDto userDto){
        User user = userService.update(getSessionId(authentication), userDto);
        Authentication updatedAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

    @PutMapping("/approve-member/{username}")
    public void approveMember(@PathVariable String username){
        userService.approveMember(username);
    }

    @PutMapping("/expel-member/{username}")
    public void expelMember(@PathVariable String username){
        userService.expelMember(username);
    }

    @GetMapping("waiting-list")
    public List<UserResponseDto> waitingList(Authentication authentication){
        return userService.waitingList(getSessionId(authentication));
    }

    @GetMapping("enrolled-user-list")
    public List<UserResponseDto> waintingList(Authentication authentication){
        return userService.enrolledList(getSessionId(authentication));
    }

    @DeleteMapping("delete")
    public void delete(Authentication authentication){
        userRepository.deleteById(getSessionId(authentication));
    }
}
