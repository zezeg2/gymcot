package com.example.gymcot.domain.diary;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryResponseDto {

    private Evaluation eval;

    private Exercise exercise;

    private String comment;

    private boolean isAttended;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
