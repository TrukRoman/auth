package com.example.auth.dto.user;

import java.util.List;

public record UpdateUserResponse(Long id,
                                 String firstName,
                                 String lastName,
                                 List<String> phoneNumbers) {
}
