package com.example.gymcot.domain.user;

import lombok.*;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String username;

    private String phone;

    private String email;

    private Role role;

    private boolean attendState;

    private String provider;

    private boolean enrolled;

    private Timestamp latestAttendAt;

    private Timestamp latestFinishAt;

}
