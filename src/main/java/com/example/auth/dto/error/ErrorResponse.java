package com.example.auth.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ErrorResponse(
        @Schema(description = "Error code")
        String error,

        @Schema(description = "Error message")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String description,

        @Schema(description = "Optional details depending on error type")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Map<String, List<String>> details) {

}
