package com.example.gymcot.controller;

import com.example.gymcot.domain.member.UserDto;
import com.example.gymcot.service.member.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CommonController {

    private final UserService userService;

    @ResponseBody
    @GetMapping({".", "/"})
    public String index() {
        return "index";
    }

    @ResponseBody
    @PostMapping("/join")
    public void join(@RequestBody @Valid UserDto userDto) throws IOException {
        userService.join(userDto);
    }
}
