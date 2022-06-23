package com.example.gymcot.repository;

import com.example.gymcot.domain.user.Role;
import com.example.gymcot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByEmail (String email);

    boolean existsByUsername(String username);

    List<User> findAllByRoleIs(Role role);

    List<User> findAllByGymIdAndRoleIs(Long id, Role role);

    List<User> findAllByEnrolledIsFalseAndGymIdIs(Long gymId);

    List<User> findAllByEnrolledIsTrueAndGymIdIs(Long gymId);
}
