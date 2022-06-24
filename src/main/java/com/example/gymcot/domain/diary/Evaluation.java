package com.example.gymcot.domain.diary;

import lombok.Getter;

@Getter
public enum Evaluation {
    COOL(5),
    GOOD(4),
    NORMAL(3),
    BAD(2),
    FAIL(1);

    private int score;

    Evaluation(int score) {
        this.score = score;
    }
}
