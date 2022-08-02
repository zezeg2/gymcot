package com.example.gymcot.controller.user;

import com.example.gymcot.domain.user.UserRequestDto;
import com.example.gymcot.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public void join(@RequestBody @Valid UserRequestDto userRequestDto) throws IOException {
        userService.join(userRequestDto);
    }
}
