package com.example.auth.dto.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String accessToken,
                                     Long accessExpiresIn,
                                     String refreshToken,
                                     Long refreshExpiresIn) {
}
