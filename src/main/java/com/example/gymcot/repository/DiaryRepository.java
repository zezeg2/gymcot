package com.example.gymcot.repository;

import com.example.gymcot.domain.diary.Diary;
import com.example.gymcot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByUser(Long userId);

    List<Diary> findByUserIdAndCreatedAtBetween(Long id, LocalDateTime start, LocalDateTime end);

}
