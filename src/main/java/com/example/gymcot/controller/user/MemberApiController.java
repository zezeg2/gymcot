package com.example.gymcot.controller.user;

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

@RestController
@RequestMapping("/api/v1/member")
public class MemberApiController extends UserApiController {

    public MemberApiController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        super(authenticationManager, userRepository, userService);

    }

    @GetMapping
    public UserResponseDto member(Authentication authentication) {
        return userService.getUser(getSessionId(authentication));
    }

    @PostMapping
    public void updateMember(Authentication authentication, @RequestBody @Valid UserRequestDto userDto) {
        User user = userService.update(getSessionId(authentication), userDto);
        Authentication updatedAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

    @PostMapping("/gym/{gymId}")
    public void setGym(Authentication authentication, @PathVariable Long gymId){
        userService.setGym(getSessionId(authentication), gymId);
    }

    @PostMapping("/attend")
    public void changeState(Authentication authentication) {
        userService.toggleState(getSessionId(authentication));
    }

//    @PostMapping("/set-gym")
//    public void setGym(Authentication authentication, )

    @DeleteMapping("delete")
    public void delete(Authentication authentication){
        userRepository.deleteById(getSessionId(authentication));
    }


}
