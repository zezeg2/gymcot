package com.example.gymcot.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberCreateDto {

    private String memberName;

    private String nickName;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    private String phone;

    private String password;

    @Email
    private String email;

    public Member toEntity() {
        Member member = Member.builder()
                .memberName(memberName)
                .nickName(nickName)
                .phone(phone)
                .password(password)
                .email(email)
                .build();
        return member;
    }

}
