package com.example.gymcot.domain.relation;


import com.example.gymcot.domain.diary.Exercise;
import com.example.gymcot.domain.diary.Target;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

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

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime appointmentTime;

    private Map<Target, String> detail;



    public Relation toEntity() {

        if (dtype.equals("f")) {
            FriendRelation friendRelation = new FriendRelation();
            friendRelation.setApproved(approved);
            return  friendRelation;
        } else if(dtype.equals("t")){
            TogetherRelation togetherRelation = new TogetherRelation();
            togetherRelation.setTitle(title);
            if (detail!= null){
                togetherRelation.setExercise(new Exercise(buildDetails()));
            }
            return togetherRelation;
        } else
            return null;
    }

    private String buildDetails() {
        String result = "";
        for (Map.Entry<Target, String> entry : this.detail.entrySet()) {
            Target k = entry.getKey();
            String v = entry.getValue();
            result += k.getName() + " : " + v + "\n";
        }
        return result;
    }
}