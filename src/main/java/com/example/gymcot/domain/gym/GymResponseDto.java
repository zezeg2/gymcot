package com.example.gymcot.domain.gym;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GymResponseDto {
    private Long id;

    private String title;

    private String link;

    private String category;

    private String description;

    private String telephone;

    private String address;

    private String roadAddress;

    private int mapx;

    private int mapy;

}
