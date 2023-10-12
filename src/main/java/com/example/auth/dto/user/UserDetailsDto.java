package com.example.auth.dto.user;

import java.util.List;

public record UserDetailsDto(Long id,
                             String email,
                             String firstName,
                             String lastName,
                             List<PhoneNumberDto> phoneNumbers) {
}
