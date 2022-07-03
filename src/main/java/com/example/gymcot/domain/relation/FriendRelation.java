package com.example.gymcot.domain.relation;

import com.example.gymcot.domain.user.User;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DiscriminatorValue("F")
public class FriendRelation extends Relation {
    private boolean approved;


    @Override
    public RelationResponseDto toDto() {
        return RelationResponseDto.builder()
                .id(super.getId())
//                .toUser(super.getToUser())
                .fromUsername(super.getFromUser().getUsername())
                .approved(approved).build();
    }
}
