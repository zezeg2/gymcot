package com.example.gymcot.repository;

import com.example.gymcot.domain.gym.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym,Long> {
}
