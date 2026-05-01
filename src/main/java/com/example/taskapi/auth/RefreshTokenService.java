package com.example.taskapi.auth;

import com.example.taskapi.common.exception.UnauthorizedException;
import com.example.taskapi.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refresh-token-expiry}")
    private long REFRESH_TOKEN_EXPIRY;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // Create token and wire it to user
    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRY));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    //token validation: exists, notExpired, and notRevoked
    public RefreshToken verifyToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token "));
        if (refreshToken.isRevoked() ||
                refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token expired or revoked");
        }
        return refreshToken;
    }

    // Revoke (logout)
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(t -> {
                    t.setRevoked(true);
                    refreshTokenRepository.save(t);
                });
    }

}

