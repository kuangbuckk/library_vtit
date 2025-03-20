package com.project.library.services.impl;

import com.project.library.utils.JwtTokenUtils;
import com.project.library.entities.Token;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.TokenRepository;
import com.project.library.responses.LoginResponse;
import com.project.library.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh_expiration}")
    private Long refreshTokenExpiration;

    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public Token addToken(User user, String token, boolean isMobileDevice) {

        return null;
    }

    @Override
    public String generateRefreshTokenAfterLogin(User existingUser) {
        Optional<Token> tokenOptional = tokenRepository.findByUser(existingUser);
        if (tokenOptional.isPresent()) {
            tokenRepository.delete(tokenOptional.get());
        }
        String newRefreshToken = jwtTokenUtils.generateRefreshToken(existingUser, refreshTokenExpiration);
        Token newToken = Token.builder()
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expirationDate(jwtTokenUtils.getExpirationDateFromToken(newRefreshToken))
                .issuedAt(LocalDateTime.now())
                .expired(false)
                .revoked(false)
                .user(existingUser)
                .build();
        tokenRepository.save(newToken);
        return newRefreshToken;
    }

    @Override
    public LoginResponse refreshToken(String refreshToken, User existingUser) throws Exception {
        Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);
        if (tokenOptional.isEmpty()) {
            throw new DataNotFoundException("Token not found for user with code ", existingUser.getId());
        }
        Token existingToken = tokenOptional.get();
        if (!jwtTokenUtils.validateToken(existingToken.getRefreshToken(), existingUser)) {
            tokenRepository.delete(existingToken);
            throw new Exception("Token is expired");
        }
//        else {
//            existingToken.setExpirationDate(new Date(System.currentTimeMillis()));
//            existingToken.setExpired(true);
//            existingToken.setRevoked(true);
//            tokenRepository.save(existingToken);
//        }
        String newRefreshToken = jwtTokenUtils.generateRefreshToken(existingUser, refreshTokenExpiration);

        existingToken.setRefreshToken(newRefreshToken);
        existingToken.setExpirationDate(jwtTokenUtils.getExpirationDateFromToken(newRefreshToken));
        existingToken.setIssuedAt(LocalDateTime.now());
//        Token newToken = Token.builder()
//                .refreshToken(newRefreshToken)
//                .tokenType("Bearer")
//                .expirationDate(jwtTokenUtils.getExpirationDateFromToken(newRefreshToken))
//                .issuedAt(LocalDateTime.now())
//                .user(existingUser)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(newToken);
//        if (tokenRepository.countByUser(existingUser) > Token.MAX_TOKEN_AMOUNT) {
//            Token firstTokenToDelete = tokenRepository.findFirstByUserOrderByIssuedAt(existingUser);
//            tokenRepository.delete(firstTokenToDelete);
//        }
        String newAccessToken = jwtTokenUtils.generateAccessToken(existingUser);
        return LoginResponse.builder()
                .token(newAccessToken)
                .refreshToken(existingToken.getRefreshToken())
                .build();
    }

    @Override
    public void invalidateRefreshToken(String refreshToken) {
        Optional<Token> existingToken = tokenRepository.findByRefreshToken(refreshToken);
        tokenRepository.delete(existingToken.get());
    }

    @Override
    public boolean isTokenExist(String refreshToken) {
        return tokenRepository.existsTokenByRefreshToken(refreshToken);
    }
}
