package com.example.gymcot.service;

import com.example.gymcot.domain.diary.Diary;
import com.example.gymcot.domain.diary.DiaryRequestDto;
import com.example.gymcot.domain.diary.DiaryResponseDto;
import com.example.gymcot.domain.diary.Evaluation;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.repository.DiaryRepository;
import com.example.gymcot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    public void create(User user, DiaryRequestDto diaryRequestDto) {
        if (diaryRepository.findByUserIdAndCreatedAtBetween(user.getId()
                , LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
                , LocalDateTime.of(LocalDate.now(), LocalTime.MIN)).isEmpty()) {
            Diary diary = diaryRequestDto.toEntity();
            diary.setUser(user);
            diary.setEval(null);
            diary.setTitle(user.getUsername() + "-" + LocalDate.now());
            diaryRepository.save(diary);
        } else {
            throw new IllegalArgumentException("다이어리는 하루에 하나만 작성 가능합니다.");
        }
    }

    public void evaluate(Long sessionId, int grade) {
        Diary diary = diaryRepository.findByTitleIs(userRepository.findById(sessionId).get().getUsername() + "-" + LocalDate.now());
        switch (grade) {
            case 1:
                diary.setEval(Evaluation.FAIL);
            case 2:
                diary.setEval(Evaluation.BAD);
            case 3:
                diary.setEval(Evaluation.NORMAL);
            case 4:
                diary.setEval(Evaluation.GOOD);
            case 5:
                diary.setEval(Evaluation.COOL);
        }
    }

    public void deleteDiary(Long sessionId) {
        Diary diary = diaryRepository.findByTitleIs(userRepository.findById(sessionId).get().getUsername() + "-" + LocalDate.now());
        diaryRepository.delete(diary);
    }

    public DiaryResponseDto todayDiary(String username) {
        return diaryRepository.findByTitleIs(username + "-" + LocalDate.now()).toDto();
    }

    public DiaryResponseDto findByDate(Long sessionId, LocalDate date) {
        return diaryRepository.findByUserIdAndCreatedAtBetween(sessionId, LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX)).stream().findAny().get().toDto();
    }

    public List<DiaryResponseDto> findByUnit(Long sessionId, String unit) {
        List<Diary> find = null;
        List<DiaryResponseDto> results = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        switch (unit) {

            case "w":
                find = diaryRepository.findByUserIdAndCreatedAtBetween(sessionId, now.minusDays(7), now);
                break;
            case "m":
                find = diaryRepository.findByUserIdAndCreatedAtBetween(sessionId, now.minusDays(30), now);
                break;
            case "y":
                find = diaryRepository.findByUserIdAndCreatedAtBetween(sessionId, now.minusDays(365), now);
                break;
            default:
                find.add(diaryRepository.findByUserId(sessionId));
                break;
        }
        find.forEach(o -> results.add(o.toDto()));
        return results;
    }

    public List<Boolean> monthDiary(Long sessionId, Integer month, Integer year) {
        if (month > 12 || month < 1){
            throw new IllegalArgumentException("1-12사이의 입력값이 요구됩니다.");
        }

        List<Boolean> calender = new ArrayList<>();
        LocalDate critic;
        critic = LocalDate.of(Objects.requireNonNullElseGet(year, () -> LocalDate.now().getYear()), month, 1);

        List<Diary> diaries = diaryRepository.findByUserIdAndCreatedAtBetween(sessionId
                , LocalDateTime.of(critic, LocalTime.MIN)
                , LocalDateTime.of(LocalDate.of(critic.getYear(), critic.getMonthValue(), critic.getDayOfMonth()), LocalTime.MAX));
        for (int i = 0; i < critic.getDayOfMonth(); i++){
            calender.add(false);
        }

        diaries.forEach(o -> {
            int attendDay = o.getCreatedAt().getDayOfMonth();
            calender.set(attendDay, true);
        });
        return calender;
    }

}
