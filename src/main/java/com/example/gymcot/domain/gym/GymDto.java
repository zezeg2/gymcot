package com.example.gymcot.domain.gym;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GymDto {

    private String title;

    private String link;

    private String category;

    private String description;

    private String telephone;

    private String address;

    private String roadAddress;

    private int mapx;

    private int mapy;


    public Gym toEntity(){
        return Gym.builder()
                .title(title)
                .link(link)
                .category(category)
                .description(description)
                .telephone(telephone)
                .address(address)
                .loadAddress(roadAddress)
                .mapx(mapx)
                .mapy(mapy)
                .approved(false)
                .build();
    }
}
