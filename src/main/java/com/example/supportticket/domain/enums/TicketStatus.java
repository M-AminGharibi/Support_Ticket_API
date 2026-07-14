package com.example.supportticket.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Current lifecycle status of a ticket")
public enum TicketStatus {
    OPEN,
    IN_PROGRESS,
    CLOSED
}
