package com.example.gymcot.repository;

import com.example.gymcot.domain.relation.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<Relation, Long> {
}
