package com.example.gymcot.domain.diary;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryRequestDto {
    private Evaluation eval;

    private List<Target> targets;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private Map<Target, String> detail;

    private String comment;

    public Diary toEntity() {

        return Diary.builder()
                .eval(eval)
                .exercise(new Exercise(startAt, endAt, buildDetails()))
                .comment(comment)
                .build();
    }

    private String buildDetails() {
        String result = "";
        for (Map.Entry<Target, String> entry : this.detail.entrySet()) {
            Target k = entry.getKey();
            String v = entry.getValue();
            result += k.getName() + " : " + v + "\n";
        }
        return result;
    }

}
