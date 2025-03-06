package com.project.library.services.interfaces;

import com.project.library.entities.Token;
import com.project.library.entities.User;

public interface ITokenService {
    Token addToken(User user, String token, boolean isMobileDevice);
    String generateRefreshTokenAfterLogin(User existingUser);
    String refreshToken(String refreshToken, User existingUser) throws Exception;
    void invalidateRefreshToken(String refreshToken);
    boolean isTokenExist(String refreshToken);
}
