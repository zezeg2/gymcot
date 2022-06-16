package com.example.gymcot.domain.comment;

import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.post.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
