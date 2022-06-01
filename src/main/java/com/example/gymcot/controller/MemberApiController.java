package com.example.gymcot.controller;

import com.example.gymcot.domain.member.MemberCreateDto;
import com.example.gymcot.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/join")
    public void join(@RequestBody MemberCreateDto memberCreateDto) throws IOException {
        memberService.join(memberCreateDto);
    }

    @GetMapping("/member")
    public String member(){
        return "user";
    }

    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
}
