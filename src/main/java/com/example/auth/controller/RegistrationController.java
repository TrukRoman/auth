package com.example.auth.controller;

import com.example.auth.dto.RegistrationUserRequest;
import com.example.auth.dto.error.ErrorResponse;
import com.example.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/registration")
@Tag(name = "Registration API", description = "API for registration")
public class RegistrationController {

    private final UserService userService;

    @ApiResponse(responseCode = "200",
            description = "Successful response.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema()))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request body.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409",
            description = "User with the same email already exists.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "User registration")
    @PostMapping(value = "/registration")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid RegistrationUserRequest registrationUserDto) {
        userService.register(registrationUserDto);
        return ResponseEntity.ok().build();
    }
}
