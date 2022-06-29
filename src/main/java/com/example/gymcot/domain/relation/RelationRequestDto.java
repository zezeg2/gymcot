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

    private String title;

    private String toUsername;

    private boolean approved;

    private Exercise exercise;

    public Relation toEntity() {

        if (dtype == "f") {
            FriendRelation friendRelation = new FriendRelation();
            friendRelation.setApproved(approved);
            return  friendRelation;
        } else if(dtype == "f"){
            TogetherRelation togetherRelation = new TogetherRelation();
            togetherRelation.setTitle(title);
            togetherRelation.setExercise(exercise);
            return togetherRelation;
        } else
            return null;
    }
}