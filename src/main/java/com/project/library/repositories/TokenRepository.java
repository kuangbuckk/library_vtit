package com.project.library.repositories;

import com.project.library.entities.Token;
import com.project.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findFirstByUserOrderByIssuedAtDesc(User existingUser);
    Optional<Token> findFirstByUserOrderByIssuedAtAsc(User existingUser);
    int countByUser(User existingUser);
    void deleteByRefreshToken(String refreshToken);
    boolean existsTokenByRefreshToken(String refreshToken);
}
