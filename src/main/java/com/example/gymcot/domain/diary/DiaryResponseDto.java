package com.example.gymcot.domain.diary;

import com.example.gymcot.domain.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryResponseDto {

    private Evaluation eval;

    private Exercise exercise;

    private String comment;

    private boolean isAttended;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
