package com.example.taskapi.auth;

import com.example.taskapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.time.Instant;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    long deleteByExpiresAtBefore(Instant cutoff);
    long deleteByRevokedTrue();
}
