package com.example.auth.service;

import com.example.auth.dto.auth.AuthenticationRequest;
import com.example.auth.dto.auth.AuthenticationResponse;
import com.example.auth.dto.auth.TokenRefreshRequest;
import com.example.auth.entity.AccessToken;
import com.example.auth.entity.RefreshToken;
import com.example.auth.entity.User;
import com.example.auth.exception.ServiceException;
import com.example.auth.repository.AccessTokenRepository;
import com.example.auth.repository.RefreshTokenRepository;
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
    private final AccessTokenService accessTokenService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenRepository accessTokenRepository;

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(), request.password()));

        User user = userRepository.findUserByEmail(request.email())
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        String accessJwt = jwtUtil.generateAccessToken(user);
        String refreshJwt = jwtUtil.generateRefreshToken(user);
        RefreshToken refreshToken = createRefreshToken(refreshJwt, user);
        user.getRefreshTokens().add(refreshToken);
        user.getAccessTokens().add(createAccessToken(accessJwt, user, refreshToken));

        return getAuthenticationResponse(accessJwt, refreshJwt);
    }

    @Transactional
    public AuthenticationResponse refreshToken(TokenRefreshRequest request) {
        String userEmail = jwtUtil.extractUserEmail(request.refreshToken());
        Optional<User> userByEmail = userRepository.findUserByEmail(userEmail);
        User user = userByEmail.orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken());
        boolean isRefreshTokenRevoked = jwtUtil.isRefreshTokenRevoked(refreshToken);
        if (jwtUtil.isTokenValid(request.refreshToken(), user) && !isRefreshTokenRevoked) {
            accessTokenService.revokeActiveTokensByRefreshToken(request.refreshToken());
            String accessJwt = jwtUtil.generateAccessToken(user);
            user.getAccessTokens().add(createAccessToken(accessJwt, user, refreshToken));
            return getAuthenticationResponse(accessJwt, request.refreshToken());
        } else {
            throw new ServiceException(REFRESH_TOKEN_EXPIRED);
        }
    }

    private AccessToken createAccessToken(String accessJwt,
                                          User user,
                                          RefreshToken refreshToken) {
        AccessToken accessToken = AccessToken.builder()
                .token(accessJwt)
                .refreshToken(refreshToken)
                .user(user)
                .build();

        return accessTokenRepository.save(accessToken);
    }

    private RefreshToken createRefreshToken(String refreshJwt,
                                            User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshJwt)
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private AuthenticationResponse getAuthenticationResponse(String accessJwt,
                                                             String refreshJwt) {
        return AuthenticationResponse.builder()
                .accessToken(accessJwt)
                .accessExpiresIn(jwtUtil.getAccessTokenExpiration())
                .refreshToken(refreshJwt)
                .refreshExpiresIn(jwtUtil.getRefreshTokenExpiration())
                .build();
    }
}
