package com.example.gymcot.domain.team;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Team
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int teamSize;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id")
    private List<TeamMember> teamMemberList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private User user;
}
