package com.example.auth.dto;

import com.example.auth.validator.password.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record NewPasswordRequest(@NotBlank String token,
                                 @ValidPassword String newPassword) {
}
