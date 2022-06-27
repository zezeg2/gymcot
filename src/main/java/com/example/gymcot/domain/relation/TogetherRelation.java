package com.example.gymcot.domain.relation;

import com.example.gymcot.domain.diary.Exercise;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DiscriminatorValue("T")
public class TogetherRelation extends Relation {

    private String title;

    @Embedded
    private Exercise exercise;

    private boolean completed;

    @Override
    public RelationResponseDto toDto() {
        return RelationResponseDto.builder()
                .id(super.getId())
                .title(title)
                .toUser(super.getToUser())
                .exercise(exercise)
                .build();
    }
}
