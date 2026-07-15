package com.example.supportticket.ticket.repository;

import com.example.supportticket.ticket.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
