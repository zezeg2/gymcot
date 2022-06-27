package com.example.gymcot.domain.relation;

import com.example.gymcot.domain.diary.Exercise;
import com.example.gymcot.domain.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelationResponseDto {

    private Long id;

    private String title;

    private User toUser;

    private boolean approved;

    private Exercise exercise;
}
