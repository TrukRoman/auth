package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.auth.config.JwtAuthenticationFilter.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return;
        }
        User user = SecurityUtils.getUser();
        accessTokenService.revokeActiveTokens(user.getId());
        refreshTokenService.revokeActiveTokens(user.getId());
        SecurityContextHolder.clearContext();
    }
}
