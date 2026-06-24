package com.order_management.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order_management.demo.entity.RefreshToken;
import com.order_management.demo.entity.User;
import com.order_management.demo.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(User user){
    RefreshToken refreshToken =new RefreshToken();

    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken.setUser(user);

    refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

    refreshToken.setRevoked(false);

    return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {

    RefreshToken refreshToken =refreshTokenRepository.findByToken(token).
    orElseThrow(() ->new RuntimeException("Invalid refresh token"));

    if (refreshToken.isRevoked()) {
        throw new RuntimeException( "Refresh token revoked");
    }

    if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        throw new RuntimeException( "Refresh token expired");
    }
   

    return refreshToken;
}

public void revokeRefreshToken(
        String token) {

    RefreshToken refreshToken =
            refreshTokenRepository
                    .findByToken(token)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Invalid refresh token"));

    refreshToken.setRevoked(true);

    refreshTokenRepository
            .save(refreshToken);
}

public void revokeAllUserTokens(
        User user) {

    List<RefreshToken> tokens =
            refreshTokenRepository
                    .findByUserAndRevokedFalse(
                            user);

    tokens.forEach(token ->
            token.setRevoked(true));

    refreshTokenRepository
            .saveAll(tokens);
}



}
