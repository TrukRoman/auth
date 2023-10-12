package com.example.auth.dto.registration;

import com.example.auth.validator.email.ValidEmail;
import com.example.auth.validator.password.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record RegistrationUserRequest(@ValidEmail String email,
                                      @ValidPassword String password,
                                      @NotBlank @Length(min = 1, max = 100) String firstName,
                                      @NotBlank @Length(min = 1, max = 100) String lastName,
                                      @Size(max = 20) List<String> phoneNumbers) {
}
