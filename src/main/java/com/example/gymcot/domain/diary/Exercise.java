package com.example.gymcot.domain.diary;

import lombok.*;

import javax.persistence.Embeddable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Exercise {

    private Target target;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String details;

}
