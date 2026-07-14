package com.example.supportticket.exception;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long ticketId) {
        super("Ticket with id " + ticketId + " was not found");
    }
}
