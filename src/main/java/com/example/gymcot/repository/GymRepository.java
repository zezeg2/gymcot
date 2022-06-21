package com.example.gymcot.repository;

import com.example.gymcot.domain.gym.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface GymRepository extends JpaRepository<Gym,Long> {

//    List<Gym> findAllByNameContainsAndAddressContains(String title, String location);
//
//    List<Gym> findAllByNameContainsAndLoadAddressContains(String name, String location);

    List<Gym> findAllByTitleContains(String name);

    List<Gym> findAllByApprovedIsFalse();

    List<Gym> findAllByApprovedIsTrue();
}
