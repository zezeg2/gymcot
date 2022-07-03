package com.example.gymcot.repository;

import com.example.gymcot.domain.relation.TogetherRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TogetherRelationRepository extends JpaRepository<TogetherRelation, Long> {

    List<TogetherRelation> findAllByToUserIdAndCompletedIs(boolean completed);
}
