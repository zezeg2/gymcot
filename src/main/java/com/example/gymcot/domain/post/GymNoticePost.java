package com.example.gymcot.domain.post;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("GN")
public class GymNoticePost extends Post {
}
