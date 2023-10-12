package com.example.auth.dto.password;

import com.example.auth.validator.password.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(@NotBlank @ValidPassword String newPassword,
                                    @NotBlank String previousPassword) {
}
