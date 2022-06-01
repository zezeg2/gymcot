package com.example.gymcot.domain.post;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("AN")
public class AppNoticePost extends Post {
}
