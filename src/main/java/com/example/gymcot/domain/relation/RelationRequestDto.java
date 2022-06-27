package com.example.gymcot.domain.relation;


import com.example.gymcot.domain.diary.Exercise;
import com.example.gymcot.domain.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelationRequestDto {

    private String dtype;

    private Long toUsername;

    private boolean approved;

    private Exercise exercise;

    public Relation toEntity() {
        Relation base = Relation.builder().build();
        if (dtype == "f") {
            FriendRelation friendRelation = (FriendRelation) base;
            friendRelation.setApproved(approved);
            return  friendRelation;
        } else if(dtype == "f"){
            TogetherRelation togetherRelation = (TogetherRelation) base;
            togetherRelation.setExercise(exercise);
            return togetherRelation;
        } else
            return null;
    }
}