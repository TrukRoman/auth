package com.example.auth.controller;

import com.example.auth.dto.user.UpdateUserRequest;
import com.example.auth.dto.user.UpdateUserResponse;
import com.example.auth.dto.user.UserDetailsDto;
import com.example.auth.dto.user.UserInfoDto;
import com.example.auth.dto.error.ErrorResponse;
import com.example.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.auth.config.OpenAPIConfiguration.BEARER_AUTHENTICATION;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@Tag(name = "Admin API", description = "API for administrator")
public class AdminController {

    private final UserService userService;

    @ApiResponse(responseCode = "200",
            description = "Successful response with page of users.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Page.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request parameters.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Get all users")
    @GetMapping(value = "/user/all")
    public ResponseEntity<Page<UserInfoDto>> getAllUserList(
            @PageableDefault Pageable pageable) {
        Page<UserInfoDto> userList = userService.getAllUsersWithUserRole(pageable);
        return ResponseEntity.ok(userList);
    }

    @ApiResponse(responseCode = "200",
            description = "Successful response with User's details.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDetailsDto.class)))
    @ApiResponse(responseCode = "404",
            description = "User not found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Get user's details by email")
    @GetMapping(value = "/user/details")
    public ResponseEntity<UserDetailsDto> getUserDetails(@RequestParam String email) {
        UserDetailsDto userDetailsDto = userService.getUserDetails(email);
        return ResponseEntity.ok(userDetailsDto);
    }

    @ApiResponse(responseCode = "200",
            description = "Successful response with User's details.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Page.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Get users by firstname and lastname")
    @GetMapping(value = "/user/search")
    public ResponseEntity<Page<UserDetailsDto>> searchUserByFirstNameAndLastName(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @PageableDefault Pageable pageable) {
        Page<UserDetailsDto> userDetails = userService.searchUserByFirstNameAndLastName(firstname, lastname, pageable);
        return ResponseEntity.ok(userDetails);
    }

    @ApiResponse(responseCode = "200",
            description = "Successful response with updated User's details.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateUserResponse.class)))
    @ApiResponse(responseCode = "404",
            description = "User not found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Update user data")
    @PutMapping(value = "/user/update")
    public ResponseEntity<UpdateUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        UpdateUserResponse updateUserResponse = userService.updateUserInfoById(request);
        return ResponseEntity.ok(updateUserResponse);
    }

    @ApiResponse(responseCode = "200",
            description = "Successful response.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema()))
    @ApiResponse(responseCode = "400",
            description = "Admin user cannot be deleted.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error in the request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(method = "Remove users")
    @DeleteMapping(value = "/user/remove")
    public ResponseEntity<Void> removeUsers(@RequestParam List<Long> ids) {
        userService.removeUsers(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
