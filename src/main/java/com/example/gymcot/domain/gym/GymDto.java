package com.example.gymcot.domain.gym;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GymDto {

    @NotNull
    private String name;

    @NotNull
    private String location;

    public Gym toEntity(){
        return Gym.builder()
                .name(name)
                .location(location)
                .approved(false)
                .build();
    }
}
