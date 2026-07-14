package com.example.supportticket.service;

import com.example.supportticket.domain.dto.request.CreateTicketRequest;
import com.example.supportticket.domain.dto.response.TicketResponse;
import com.example.supportticket.domain.enums.TicketStatus;

import java.util.List;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    List<TicketResponse> getAllTickets();

    TicketResponse getTicketById(Long id);

    TicketResponse updateStatus(Long id, TicketStatus status);
}
