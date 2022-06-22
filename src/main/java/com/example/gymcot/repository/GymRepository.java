package com.example.gymcot.repository;

import com.example.gymcot.domain.gym.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Null;
import java.util.List;

@Repository
public interface GymRepository extends JpaRepository<Gym,Long> {

//    List<Gym> findAllByNameContainsAndAddressContains(String title, String location);
//
//    List<Gym> findAllByNameContainsAndLoadAddressContains(String name, String location);

    List<Gym> findAllByTitleContains(String name);

    List<Gym> findAllByApprovedIsFalse();

    List<Gym> findAllByApprovedIsTrue();

    List<Gym> findAllByTitleContainsAndAddressContainsAndApprovedIsTrue(String title, String address);

    List<Gym> findAllByTitleContainsAndRoadAddressContainsAndApprovedIsTrue(String title, String roadAddress);

    Gym findByUserId(Long userId);
}
