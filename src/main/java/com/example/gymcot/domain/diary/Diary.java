package com.example.gymcot.domain.diary;

import com.example.gymcot.domain.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private Evaluation eval;

    @Embedded
    private Exercise exercise;

    private String comment;

    private boolean isAttended;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public DiaryResponseDto toDto() {
        return DiaryResponseDto.builder()
                .eval(eval)
                .exercise(exercise)
                .comment(comment)
                .isAttended(isAttended)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    ;

}
