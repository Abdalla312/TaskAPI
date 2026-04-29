package com.example.taskapi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    boolean existsUserByUsername(String username);

    @Query("select u from User u where u.username = :identifier or u.email = :identifier ")
    Optional<User> findByUsernameOrEmail(String identifier);
}