package com.example.gymcot.domain.diary;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryRequestDto {
    private Evaluation eval;

    private Exercise exercise;

    private String comment;

    public Diary toEntity(){
        return Diary.builder()
                .eval(eval)
                .exercise(exercise)
                .comment(comment)
                .build();
    }
}
