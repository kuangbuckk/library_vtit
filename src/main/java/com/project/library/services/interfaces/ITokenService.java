package com.project.library.services.interfaces;

import com.project.library.entities.Token;
import com.project.library.entities.User;

public interface ITokenService {
    Token addToken(User user, String token, boolean isMobileDevice);
    String generateRefreshToken(User existingUser);
    void invalidateRefreshToken(String refreshToken);
    boolean isTokenExist(String refreshToken);
}
