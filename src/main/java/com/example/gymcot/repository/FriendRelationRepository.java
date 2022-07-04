package com.example.gymcot.repository;

import com.example.gymcot.domain.relation.FriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {

//    FriendRelation findByFromUser_IdAndToUser_Username(Long fromUserId, String toUsername);
    Optional<FriendRelation> findByFromUserIdAndToUser_Username(Long fromUserId, String toUsername);

    List<FriendRelation> findByToUserIdAndApprovedIs(Long userId, boolean approved);

    Optional<FriendRelation> findByFromUser_UsernameAndToUserId(String username, Long toUserId);
}
