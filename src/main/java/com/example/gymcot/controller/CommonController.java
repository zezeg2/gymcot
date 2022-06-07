package com.example.gymcot.controller;

import com.example.gymcot.domain.member.UserDto;
import com.example.gymcot.service.member.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommonController {

    private final UserService userService;

    @GetMapping({".", "/"})
    public String index() {
        return "index";
    }

    @PostMapping("/join")
    public void join(@RequestBody @Valid UserDto userDto) throws IOException {
        userService.join(userDto);
    }
}
