package com.example.auth.controller;

import com.example.auth.dto.ChangePasswordRequest;
import com.example.auth.dto.NewPasswordRequest;
import com.example.auth.dto.error.ErrorResponse;
import com.example.auth.service.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.auth.config.OpenAPIConfiguration.BEARER_AUTHENTICATION;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/password")
@Tag(name = "Password API", description = "API for operation with password")
public class PasswordController {

    private final PasswordService passwordService;

    @ApiResponse(responseCode = "200",
            description = "Successful response.")
    @ApiResponse(responseCode = "404",
            description = "User not found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Password recovery request")
    @GetMapping(value = "/recovery/request")
    public ResponseEntity<Void> recoveryPasswordSendRequest(@RequestParam String email) {
        passwordService.recoveryPasswordSendRequest(email);
        return ResponseEntity.ok().build();
    }

    @ApiResponse(responseCode = "200",
            description = "Successful response.")
    @ApiResponse(responseCode = "404",
            description = "User not found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401",
            description = "Token validation failed. Please provide a valid token.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request body.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Set new password for user")
    @PostMapping(value = "/recovery/password")
    public ResponseEntity<Void> setNewPassword(@RequestBody @Valid NewPasswordRequest request) {
        passwordService.setNewPassword(request);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = BEARER_AUTHENTICATION)
    @ApiResponse(responseCode = "200",
            description = "Successful response.")
    @ApiResponse(responseCode = "401",
            description = "Unauthorized access. Please provide a valid token.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Incorrect previous password.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request body.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Change password")
    @PostMapping(value = "/change")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        passwordService.changePassword(changePasswordRequest);
        return ResponseEntity.ok().build();
    }

}
