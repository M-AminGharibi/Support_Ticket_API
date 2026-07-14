package com.example.supportticket.controller;

import com.example.supportticket.common.ApiResponse;
import com.example.supportticket.common.ErrorResponse;
import com.example.supportticket.domain.dto.request.CreateTicketRequest;
import com.example.supportticket.domain.dto.request.UpdateStatusRequest;
import com.example.supportticket.domain.dto.response.TicketResponse;
import com.example.supportticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Tickets", description = "Create, retrieve, and update support tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @Operation(
            summary = "Create a ticket",
            description = "Creates a support ticket from a non-empty title and description. The initial status is OPEN."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Ticket created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(
            @Valid @RequestBody CreateTicketRequest request
    ) {
        TicketResponse ticket = ticketService.createTicket(request);
        ApiResponse<TicketResponse> response = ApiResponse.success("Ticket created successfully", ticket);
        return ResponseEntity.created(URI.create("/tickets/" + ticket.id())).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Get all tickets",
            description = "Returns every support ticket regardless of its current status."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tickets retrieved successfully"
    )
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllTickets() {
        List<TicketResponse> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(ApiResponse.success("Tickets retrieved successfully", tickets));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a ticket by ID",
            description = "Returns the support ticket identified by the supplied long ID."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Ticket retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(@PathVariable Long id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ApiResponse.success("Ticket retrieved successfully", ticket));
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Update ticket status",
            description = "Changes the status of an existing support ticket."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Ticket status updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Status is missing or invalid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<TicketResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        TicketResponse ticket = ticketService.updateStatus(id, request.status());
        return ResponseEntity.ok(ApiResponse.success("Ticket status updated successfully", ticket));
    }
}
