package com.example.gymcot.controller;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.member.MemberCreateDto;
import com.example.gymcot.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    private MemberService userService;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principal){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("authentication.getPrincipal().getAttributes : {}", principalDetails.getMember());
        log.info("using @AuthenticationPrincipal & principalDetails.getAttributes: {}" , principal.getMember());
        return "세션정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String oauthLoginTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth){

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("authentication.getPrincipal().getAttributes : {}", oAuth2User.getAttributes());
        log.info("using @AuthenticationPrincipal & oAuthUser.getAttributes : {}" , oAuth.getAttributes());
        return "OAuth 세션정보 확인하기";
    }

    @GetMapping({".", "/"})
    public String index() {
        // mustache 기본폴더 : src/main/resources
        // view resolver 설정 : templates(prefix), mustache(suffix) -> 기본설정이므로 생략가능
        return "index";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/user")
    public @ResponseBody
    String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails.getUser() : {} " , principalDetails.getMember() );
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody
    String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody
    String manager() {
        return "manager";
    }

    //security config 파일 작성후에 작동함

    @PostMapping("/join")
    public String join(@Valid MemberCreateDto memberCreateDto) {
        userService.join(memberCreateDto);
        return "redirect:/loginForm";
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "개인정보";
    }


}
