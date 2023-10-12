package com.example.auth.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UserInfoEditRequest(@NotBlank @Length(min = 1, max = 100) String firstName,
                                  @NotBlank @Length(min = 1, max = 100) String lastName,
                                  @Size(max = 20) List<String> phoneNumbers) {
}
