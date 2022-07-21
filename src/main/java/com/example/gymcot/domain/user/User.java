package com.example.gymcot.domain.user;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.relation.Relation;
import com.example.gymcot.domain.team.TeamMember;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String OAuthId;

    private String username;

    private String phone;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean attendState;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime latestAttendAt;

    private LocalDateTime latestFinishAt;

    private String provider;

    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "relation_id")
    private List<Relation> relation;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id")
    private List<TeamMember> teamMemberList;

    private boolean enrolled;

    public UserResponseDto toDto(){
        return UserResponseDto.builder()
                .id(id)
                .username(username)
                .phone(phone)
                .email(email)
                .role(role)
                .attendState(attendState)
                .enrolled(enrolled)
                .latestAttendAt(latestAttendAt)
                .latestFinishAt(latestFinishAt)
                .build();
    }
}
