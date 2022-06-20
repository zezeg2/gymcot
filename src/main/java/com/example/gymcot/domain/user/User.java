package com.example.gymcot.domain.user;

import com.example.gymcot.domain.gym.Gym;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

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

    private boolean attendState = false;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    private String provider;

    private String providerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;
}