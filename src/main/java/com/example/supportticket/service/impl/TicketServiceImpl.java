package com.example.supportticket.service.impl;

import com.example.supportticket.aop.LogExecutionTime;
import com.example.supportticket.domain.dto.request.CreateTicketRequest;
import com.example.supportticket.domain.dto.response.TicketResponse;
import com.example.supportticket.domain.entity.Ticket;
import com.example.supportticket.domain.enums.TicketStatus;
import com.example.supportticket.exception.TicketNotFoundException;
import com.example.supportticket.mapper.TicketMapper;
import com.example.supportticket.repository.TicketRepository;
import com.example.supportticket.service.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    @Transactional
    @LogExecutionTime
    public TicketResponse createTicket(CreateTicketRequest request) {
        Ticket ticket = ticketMapper.toEntity(request);
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toResponse(savedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<TicketResponse> getAllTickets() {
        return ticketMapper.toResponses(ticketRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public TicketResponse getTicketById(Long id) {
        return ticketMapper.toResponse(findTicket(id));
    }

    @Override
    @Transactional
    @LogExecutionTime
    public TicketResponse updateStatus(Long id, TicketStatus status) {
        Ticket ticket = findTicket(id);
        ticket.updateStatus(status);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ticketMapper.toResponse(updatedTicket);
    }

    private Ticket findTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }
}
