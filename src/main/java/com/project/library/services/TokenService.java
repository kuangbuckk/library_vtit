package com.project.library.services.interfaces;

import com.project.library.entities.Token;
import com.project.library.entities.User;
import com.project.library.responses.LoginResponse;

public interface ITokenService {
    Token addToken(User user, String token, boolean isMobileDevice);
    String generateRefreshTokenAfterLogin(User existingUser);
    LoginResponse refreshToken(String refreshToken, User existingUser) throws Exception;
    void invalidateRefreshToken(String refreshToken);
    boolean isTokenExist(String refreshToken);
}
