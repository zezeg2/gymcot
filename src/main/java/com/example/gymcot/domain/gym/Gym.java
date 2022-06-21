package com.example.gymcot.domain.gym;

import com.example.gymcot.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private boolean approved;

    private Long userId;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public GymResponseDto toDto(){
        return GymResponseDto.builder()
                .id(id)
                .title(title)
                .link(link)
                .category(category)
                .description(description)
                .telephone(telephone)
                .address(address)
                .roadAddress(roadAddress)
                .mapx(mapx)
                .mapy(mapy)
                .build();
    }
}
