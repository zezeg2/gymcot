package com.example.gymcot.domain.post;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("G")
public class GatheringPost extends Post {
}
