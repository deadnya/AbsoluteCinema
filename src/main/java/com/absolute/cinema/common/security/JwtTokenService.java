package com.absolute.cinema.common.security;

import com.absolute.cinema.entity.Token;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-expiration-ms}")
    private long accessExpirationMs;

    private final TokenRepository tokenRepository;

    public String generateToken(User user) {
        String primaryRole = user.getRoles().stream()
                .findFirst()
                .map(role -> role.getRole().name())
                .orElse("UNKNOWN");

        return Jwts
                .builder()
                .subject(user.getEmail())
                .claim("role", primaryRole)
                .claim("userId", user.getId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean isTokenInDatabase = tokenRepository
                .findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);
        
        return username.equals(userDetails.getUsername()) 
               && !isTokenExpired(token) 
               && isTokenInDatabase;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Scheduled(fixedRateString = "${jwt.expired-tokens-cleanup-ms}")
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpirationDateBefore(Instant.now());
    }

    public void revokeToken(String accessToken) {
        tokenRepository.findByAccessToken(accessToken)
                .ifPresent(token -> {
                    token.setLoggedOut(true);
                    tokenRepository.save(token);
                });
    }

    public void revokeAllTokens(User user) {
        List<Token> tokens = tokenRepository.findAllByUserAndLoggedOutFalse(user);
        tokens.forEach(token -> {
            token.setLoggedOut(true);
            tokenRepository.save(token);
        });
    }

    public Token saveToken(User user, String accessToken) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setUser(user);
        token.setLoggedOut(false);
        token.setExpirationDate(extractExpiration(accessToken).toInstant());
        return tokenRepository.save(token);
    }
}