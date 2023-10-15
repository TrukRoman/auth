package com.example.auth.controller;

import com.example.auth.dto.user.AdminInfoResponse;
import com.example.auth.dto.user.UserDetailsDto;
import com.example.auth.dto.user.UserInfoDto;
import com.example.auth.dto.user.UserInfoEditRequest;
import com.example.auth.dto.error.ErrorResponse;
import com.example.auth.entity.User;
import com.example.auth.service.UserService;
import com.example.auth.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.auth.config.OpenAPIConfiguration.BEARER_AUTHENTICATION;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Tag(name = "User API", description = "Controller for working with user")
public class UserController {

    private final UserService userService;

    @SecurityRequirement(name = BEARER_AUTHENTICATION)
    @ApiResponse(responseCode = "200",
            description = "Successful response with user's information.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDetailsDto.class)))
    @ApiResponse(responseCode = "401",
            description = "Unauthorized - Authentication failure.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403",
            description = "Forbidden - Access denied.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404",
            description = "User not found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Get user info")
    @GetMapping(value = "/info")
    public ResponseEntity<UserDetailsDto> getUserInfo() {
        UserDetailsDto userDetailsDto = userService.getUserInfo(SecurityUtils.getUser().getEmail());
        return ResponseEntity.ok(userDetailsDto);
    }

    @SecurityRequirement(name = BEARER_AUTHENTICATION)
    @ApiResponse(responseCode = "200",
            description = "Successful response with updated user information.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserInfoDto.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request body.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401",
            description = "Unauthorized - Authentication failure.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403",
            description = "Forbidden - Access denied.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Edit user info")
    @PutMapping(value = "/info/edit")
    public ResponseEntity<UserInfoDto> editUserInfo(@RequestBody UserInfoEditRequest userInfoEditRequest) {
        User user = SecurityUtils.getUser();
        UserInfoDto userInfoDto = userService.updateCurrentUserInfo(userInfoEditRequest, user.getId());
        return ResponseEntity.ok(userInfoDto);
    }

    @SecurityRequirement(name = BEARER_AUTHENTICATION)
    @ApiResponse(responseCode = "200",
            description = "Successful response with admin user's information.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AdminInfoResponse.class)))
    @ApiResponse(responseCode = "401",
            description = "Unauthorized - Authentication failure.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403",
            description = "Forbidden - Access denied.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404",
            description = "User with role admin not found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Get user info")
    @GetMapping(value = "/admin/info")
    public ResponseEntity<AdminInfoResponse> getAdminInfo() {
        AdminInfoResponse adminInfo = userService.getAdminInfo();
        return ResponseEntity.ok(adminInfo);
    }

}
