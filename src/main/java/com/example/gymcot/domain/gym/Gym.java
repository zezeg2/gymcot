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

    private String loadAddress;

    private int mapx;

    private int mapy;

    private boolean approved;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
}
