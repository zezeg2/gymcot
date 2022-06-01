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

    @Embedded
    private Exercise exercise;

    private boolean completed;
}
