package com.project.library.services;

import com.project.library.components.JwtTokenUtils;
import com.project.library.entities.Token;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.TokenRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.services.interfaces.ITokenService;
import com.project.library.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceImpl implements ITokenService {

    @Value("${jwt.expiration}")
    private Long expiration;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public Token addToken(User user, String token, boolean isMobileDevice) {

        return null;
    }

    @Override
    public String generateRefreshToken(User existingUser) {
        Optional<Token> latestTokenOptional = tokenRepository.findFirstByUserOrderByIssuedAtDesc(existingUser);
        Date expirationDateTime;
        if (latestTokenOptional.isPresent()) {
            Token latestToken = latestTokenOptional.get();
            expirationDateTime = jwtTokenUtils.getExpirationDateFromToken(latestToken.getRefreshToken());
        } else {
            expirationDateTime = new Date(System.currentTimeMillis() + expiration * 1000L);
        }

        String newRefreshToken = jwtTokenUtils.generateRefreshToken(existingUser, expirationDateTime);
        Token newToken = Token.builder()
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .issuedAt(LocalDateTime.now())
                .expired(false)
                .revoked(false)
                .user(existingUser)
                .build();
        tokenRepository.save(newToken);
        if (tokenRepository.countByUser(existingUser) >= Token.MAX_TOKEN_AMOUNT) {
            this.invalidateRefreshToken(tokenRepository
                    .findFirstByUserOrderByIssuedAtAsc(existingUser)
                    .get()
                    .getRefreshToken()
            );
        }
        return newRefreshToken;
    }

    @Override
    public void invalidateRefreshToken(String refreshToken) {
        tokenRepository.deleteByRefreshToken(refreshToken);
    }

    @Override
    public boolean isTokenExist(String refreshToken) {
        return tokenRepository.existsTokenByRefreshToken(refreshToken);
    }
}
