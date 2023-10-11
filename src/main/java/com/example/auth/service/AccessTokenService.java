package com.example.auth.service;

import com.example.auth.repository.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;

    @Transactional
    public void revokeActiveTokens(Long userId) {
        accessTokenRepository.findActiveTokens(userId)
                .forEach(token -> token.setRevoked(Boolean.TRUE));
    }
}
