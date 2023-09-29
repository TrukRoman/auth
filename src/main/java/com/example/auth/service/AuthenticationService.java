package com.example.auth.service;

import com.example.auth.dto.AuthenticationRequest;
import com.example.auth.dto.AuthenticationResponse;
import com.example.auth.dto.TokenRefreshRequest;
import com.example.auth.entity.User;
import com.example.auth.exception.ServiceException;
import com.example.auth.repository.UserRepository;
import com.example.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.auth.exception.ExceptionType.REFRESH_TOKEN_EXPIRED;
import static com.example.auth.exception.ExceptionType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(), request.password()));

        User user = userRepository.findUserByEmail(request.email())
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .accessExpiresIn(jwtUtil.getAccessTokenExpiration())
                .refreshToken(refreshToken)
                .refreshExpiresIn(jwtUtil.getRefreshTokenExpiration())
                .build();
    }

    @Transactional
    public AuthenticationResponse refreshToken(TokenRefreshRequest request) {
        String userEmail = jwtUtil.extractUserEmail(request.refreshToken());
        Optional<User> userByEmail = userRepository.findUserByEmail(userEmail);
        User user = userByEmail.orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        boolean isRefreshTokenRevoked = jwtUtil.isRefreshTokenRevoked(userByEmail, request.refreshToken());
        if (jwtUtil.isTokenValid(request.refreshToken(), user) && !isRefreshTokenRevoked) {
            String accessToken = jwtUtil.generateAccessToken(user);
            user.setAccessToken(accessToken);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .accessExpiresIn(jwtUtil.getAccessTokenExpiration())
                    .refreshToken(request.refreshToken())
                    .refreshExpiresIn(jwtUtil.getRefreshTokenExpiration())
                    .build();
        } else {
            throw new ServiceException(REFRESH_TOKEN_EXPIRED);
        }
    }
}
