package com.example.gymcot.controller.diary;

import com.example.gymcot.config.auth.PrincipalDetails;
import com.example.gymcot.domain.diary.DiaryRequestDto;
import com.example.gymcot.domain.diary.DiaryResponseDto;
import com.example.gymcot.repository.UserRepository;
import com.example.gymcot.service.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/today")
    public DiaryResponseDto today(Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return diaryService.todayDiary(principal.getUser().getUsername());
    }

    @GetMapping("when/{date}")
    public DiaryResponseDto findByDate(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return diaryService.findByDate(getSessionId(authentication), date);
    }

    @GetMapping("/{unit}")
    public List<DiaryResponseDto> findByUnit(Authentication authentication, @PathVariable String unit){
        return diaryService.findByUnit(getSessionId(authentication), unit);
    }

    @GetMapping("/calender/{month}")
    public List<Boolean> monthDiary(Authentication authentication, @PathVariable Integer month, @RequestParam(required = false) Integer year){
        return diaryService.monthDiary(getSessionId(authentication), month, year);
    }
}
