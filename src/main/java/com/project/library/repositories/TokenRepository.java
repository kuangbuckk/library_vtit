package com.project.library.repositories;

import com.project.library.entities.Token;
import com.project.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User existingUser);
    Optional<Token> findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);
    Token findFirstByUserOrderByIssuedAt(User existingUser);
    boolean existsTokenByRefreshToken(String refreshToken);
    int countByUser(User existingUser);
}
