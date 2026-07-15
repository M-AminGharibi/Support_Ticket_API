package com.example.supportticket.ticket.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @Schema(description = "Short summary of the issue", example = "Unable to sign in")
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Schema(
                description = "Detailed explanation of the issue",
                example = "The sign-in page rejects valid credentials"
        )
        @NotBlank(message = "Description is required")
        @Size(max = 5000, message = "Description must not exceed 5000 characters")
        String description
) {
}
