package com.example.gymcot.domain.diary;

import com.example.gymcot.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    private Evaluation eval;

    @Embedded
    private Exercise exercise;

    private String comment;

    private boolean isAttended;
}
