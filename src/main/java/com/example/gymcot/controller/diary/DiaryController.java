package com.example.gymcot.controller.diary;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.diary.Diary;
import com.example.gymcot.domain.diary.DiaryRequestDto;
import com.example.gymcot.domain.diary.Evaluation;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final UserRepository userRepository;

    private final DiaryService diaryService;

    public Long getSessionId(Authentication authentication){
        return ((PrincipalDetails) authentication.getPrincipal()).getUser().getId();
    }

    @PostMapping
    public void createDiary(Authentication authentication, @RequestBody DiaryRequestDto diary){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        diaryService.create(principal.getUser(), diary);
    }

    @PutMapping("/eval{grade}")
    public void evaluate(Authentication authentication, @PathVariable int grade){
        diaryService.evaluate(getSessionId(authentication), grade);
    }

    @DeleteMapping("/delete")
    public void deleteDiary(Authentication authentication){
        diaryService.deleteDiary(getSessionId(authentication));
    }
}
