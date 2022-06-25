package com.example.gymcot.domain.diary;

import lombok.*;

import javax.persistence.Embeddable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Exercise {

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String details;

}
