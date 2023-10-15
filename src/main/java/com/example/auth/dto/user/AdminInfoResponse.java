package com.example.auth.dto.user;

import java.util.List;

public record AdminInfoResponse(String email,
                                String firstName,
                                String lastName,
                                List<String> phoneNumbers) {
}
