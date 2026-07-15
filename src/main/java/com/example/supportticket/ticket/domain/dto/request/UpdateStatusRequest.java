package com.example.supportticket.ticket.domain.dto.request;

import com.example.supportticket.ticket.domain.enums.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @Schema(description = "New ticket status", example = "IN_PROGRESS")
        @NotNull(message = "Status is required")
        TicketStatus status
) {
}
