package com.example.gymcot.domain.relation;

import com.example.gymcot.domain.diary.Exercise;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DiscriminatorValue("T")
public class TogetherRelation extends Relation {

    private String title;

    private String content;

    private LocalDateTime appointmentTime;

    @Embedded
    private Exercise exercise;

    private boolean completed;

    @Override
    public RelationResponseDto toDto() {
        return RelationResponseDto.builder()
                .id(super.getId())
                .title(title)
                .content(content)
//                .toUser(super.getToUser())
                .appointmentTime(appointmentTime)
                .fromUsername(super.getFromUser().getUsername())
                .exercise(exercise)
                .build();
    }
}
