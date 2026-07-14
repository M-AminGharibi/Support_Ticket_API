package com.example.supportticket.domain.dto.response;

import com.example.supportticket.domain.enums.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record TicketResponse(
        @Schema(example = "42") Long id,
        String title,
        String description,
        TicketStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
