package com.example.auth.dto;

import com.example.auth.validator.email.ValidEmail;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(@ValidEmail String email,
                                    @NotBlank String password) {
}
