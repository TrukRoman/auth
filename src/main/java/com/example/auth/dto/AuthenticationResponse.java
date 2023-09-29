package com.example.auth.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String accessToken,
                                     Long accessExpiresIn,
                                     String refreshToken,
                                     Long refreshExpiresIn) {
}
