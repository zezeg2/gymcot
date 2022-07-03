package com.example.gymcot.domain.relation;

import com.example.gymcot.domain.diary.Exercise;
import com.example.gymcot.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelationResponseDto {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime appointmentTime;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String fromUsername;

    private boolean approved;

    private Exercise exercise;
}
