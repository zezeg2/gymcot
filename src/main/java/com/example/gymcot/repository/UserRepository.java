package com.example.gymcot.repository;

import com.example.gymcot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String memberName);

    List<User> findByEmail (String email);
}
