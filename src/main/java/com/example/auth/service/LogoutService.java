package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.exception.ExceptionType;
import com.example.auth.exception.ServiceException;
import com.example.auth.repository.UserRepository;
import com.example.auth.util.JwtUtil;
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

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return;
        }
        String jwt = authHeader.substring(BEARER.length());
        String userEmail = jwtUtil.extractUserEmail(jwt);
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new ServiceException(ExceptionType.USER_NOT_FOUND));
        user.setAccessToken(null);
        SecurityContextHolder.clearContext();
    }
}
