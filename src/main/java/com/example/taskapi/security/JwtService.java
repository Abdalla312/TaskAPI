package com.example.taskapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access-token-expiry}")
    private long ACCESS_TOKEN_EXPIRY;

    public String extractUserName(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(CustomUserDetails userDetails) {
        String role = userDetails
                .getAuthorities().iterator().next().getAuthority();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", userDetails.getUserId())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(getSignInKey())
                .compact();
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(SECRET_KEY));
    }

    public Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public boolean isTokenExpired(String jwtToken) {
        return (extractExpiration(jwtToken).before(new Date()));
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        String username = extractUserName(jwtToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

}
