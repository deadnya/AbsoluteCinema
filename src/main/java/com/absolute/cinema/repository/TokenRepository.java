package com.absolute.cinema.repository;

import com.absolute.cinema.entity.Token;
import com.absolute.cinema.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByAccessToken(String accessToken);
    List<Token> findAllByUserAndLoggedOutFalse(User user);
    void deleteByExpirationDateBefore(Instant date);
}