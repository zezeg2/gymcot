package com.example.gymcot.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberCreateDto {

    private String username;

    private String nickName;

    private String phone;

    private String password;

    @Email
    private String email;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .nickName(nickName)
                .phone(phone)
                .password(password)
                .email(email)
                .build();
    }

}
