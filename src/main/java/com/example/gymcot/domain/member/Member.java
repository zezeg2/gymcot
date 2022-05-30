package com.example.gymcot.domain.member;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String OAuthId;
    private String memberName;
    private String nickName;
    private String phone;
    private String password;
    private String email;
    private Role role;
    @CreationTimestamp
    private Timestamp createDate;

    private String provider;
    private String providerId;
}
