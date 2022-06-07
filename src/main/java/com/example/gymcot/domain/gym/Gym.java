package com.example.gymcot.domain.gym;

import com.example.gymcot.domain.member.User;
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

    private String location;

    private String name;

    private boolean approved;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
}
