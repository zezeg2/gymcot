package com.example.gymcot.repository;

import com.example.gymcot.domain.relation.FriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {

    FriendRelation findByFromUser_IdAndToUser_Username(Long fromUserId, String toUsername);
}
