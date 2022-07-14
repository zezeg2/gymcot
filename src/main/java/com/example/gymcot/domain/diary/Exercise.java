package com.example.gymcot.domain.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Exercise {

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String details;

    public Exercise(String details) {
        this.details = details;
    }
}
