package com.example.gymcot.service.diary;

import com.example.gymcot.domain.diary.Diary;
import com.example.gymcot.domain.diary.DiaryRequestDto;
import com.example.gymcot.domain.diary.Evaluation;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.repository.DiaryRepository;
import com.example.gymcot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final UserRepository userRepository;

    private final DiaryRepository diaryRepository;

    public void create(User user, DiaryRequestDto diaryRequestDto) {
        if (diaryRepository.findByUserIdAndCreatedAtBetween(user.getId()
                , LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))
                , LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))).isEmpty()){
            Diary diary = diaryRequestDto.toEntity();
            diary.setUser(user);
            diary.setEval(null);
            diaryRepository.save(diary);
        } else{
            throw new IllegalArgumentException("다이어리는 하루에 하나만 작성 가능합니다.");
        }
    }

    public void evaluate(Long sessionId, int grade) {
        Diary diary = diaryRepository.findByUser(sessionId);
        switch (grade){
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
        Diary diary = diaryRepository.findByUser(sessionId);
        diaryRepository.delete(diary);
    }
}
