package com.example.gymcot.repository;

import com.example.gymcot.domain.relation.TogetherRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TogetherRelationRepository extends JpaRepository<TogetherRelation, Long> {

}
