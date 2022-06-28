package com.example.gymcot.repository;

import com.example.gymcot.domain.relation.FriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {

    FriendRelation findByFromUser_IdAndToUser_Username(Long fromUserId, String toUsername);

    List<FriendRelation> findByToUserIdAnAndApprovedIsTrue(Long userId);

    List<FriendRelation> findByToUserIdAnAndApprovedIsFalse(Long userId);
}
