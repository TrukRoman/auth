package com.example.auth.dto;

import java.util.List;

public record UserInfoDto(Long id,
                          String email,
                          String firstName,
                          String lastName,
                          List<String> phoneNumbers) {
}
