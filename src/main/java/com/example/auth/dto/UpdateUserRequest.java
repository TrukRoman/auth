package com.example.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UpdateUserRequest(@NotNull Long id,
                                @Length(min = 1, max = 100) String firstName,
                                @Length(min = 1, max = 100) String lastName,
                                @Size(max = 20) List<String> phoneNumbers) {
}
