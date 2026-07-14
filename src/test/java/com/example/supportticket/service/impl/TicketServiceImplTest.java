package com.example.supportticket.service.impl;

import com.example.supportticket.domain.dto.request.CreateTicketRequest;
import com.example.supportticket.domain.dto.response.TicketResponse;
import com.example.supportticket.domain.entity.Ticket;
import com.example.supportticket.domain.enums.TicketStatus;
import com.example.supportticket.exception.TicketNotFoundException;
import com.example.supportticket.mapper.TicketMapper;
import com.example.supportticket.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    private static final Long TICKET_ID = 1L;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private Ticket ticket;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void createsAndMapsTicket() {
        CreateTicketRequest request = new CreateTicketRequest("Unable to sign in", "Valid credentials are rejected");
        TicketResponse expectedResponse = ticketResponse(TicketStatus.OPEN);

        when(ticketMapper.toEntity(request)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.toResponse(ticket)).thenReturn(expectedResponse);

        TicketResponse actualResponse = ticketService.createTicket(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ticketMapper).toEntity(request);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void returnsAllMappedTickets() {
        List<Ticket> tickets = List.of(ticket);
        List<TicketResponse> expectedResponses = List.of(ticketResponse(TicketStatus.OPEN));
        when(ticketRepository.findAll()).thenReturn(tickets);
        when(ticketMapper.toResponses(tickets)).thenReturn(expectedResponses);

        List<TicketResponse> actualResponses = ticketService.getAllTickets();

        assertThat(actualResponses).isEqualTo(expectedResponses);
    }

    @Test
    void returnsTicketById() {
        TicketResponse expectedResponse = ticketResponse(TicketStatus.OPEN);
        when(ticketRepository.findById(TICKET_ID)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toResponse(ticket)).thenReturn(expectedResponse);

        TicketResponse actualResponse = ticketService.getTicketById(TICKET_ID);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void throwsWhenTicketDoesNotExist() {
        when(ticketRepository.findById(TICKET_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(TICKET_ID))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessage("Ticket with id 1 was not found");
    }

    @Test
    void updatesAndReturnsTicketStatus() {
        TicketResponse expectedResponse = ticketResponse(TicketStatus.CLOSED);
        when(ticketRepository.findById(TICKET_ID)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.toResponse(ticket)).thenReturn(expectedResponse);

        TicketResponse actualResponse = ticketService.updateStatus(TICKET_ID, TicketStatus.CLOSED);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(ticket).updateStatus(TicketStatus.CLOSED);
        verify(ticketRepository).save(ticket);
    }

    private TicketResponse ticketResponse(TicketStatus status) {
        Instant timestamp = Instant.parse("2026-07-14T08:00:00Z");
        return new TicketResponse(
                TICKET_ID,
                "Unable to sign in",
                "Valid credentials are rejected",
                status,
                timestamp,
                timestamp
        );
    }
}
