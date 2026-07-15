package com.example.supportticket.ticket.service;

import com.example.supportticket.ticket.domain.dto.request.CreateTicketRequest;
import com.example.supportticket.ticket.domain.dto.response.TicketResponse;
import com.example.supportticket.ticket.domain.enums.TicketStatus;

import java.util.List;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    List<TicketResponse> getAllTickets();

    TicketResponse getTicketById(Long id);

    TicketResponse updateStatus(Long id, TicketStatus status);
}
