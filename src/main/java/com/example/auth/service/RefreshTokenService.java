package com.example.auth.service;

import com.example.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void revokeActiveTokens(Long userId) {
        refreshTokenRepository.findActiveTokens(userId)
                .forEach(token -> token.setRevoked(Boolean.TRUE));
    }

}
