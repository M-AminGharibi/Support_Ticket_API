package com.example.supportticket.controller;

import com.example.supportticket.ticket.domain.entity.Ticket;
import com.example.supportticket.ticket.domain.enums.TicketStatus;
import com.example.supportticket.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.docker.compose.enabled=false")
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class TicketControllerIntegrationTest {

    @Container
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17-alpine");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void clearTickets() {
        ticketRepository.deleteAll();
    }

    @Test
    void createsTicketWithOpenStatusAndPersistsIt() throws Exception {
        mockMvc.perform(validCreateRequest())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("/tickets/\\d+")))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.title").value("Unable to sign in"))
                .andExpect(jsonPath("$.data.description").value("Valid credentials are rejected"))
                .andExpect(jsonPath("$.data.status").value("OPEN"));

        assertThat(ticketRepository.findAll()).singleElement().satisfies(ticket -> {
            assertThat(ticket.getStatus()).isEqualTo(TicketStatus.OPEN);
            assertThat(ticket.getCreatedAt()).isNotNull();
            assertThat(ticket.getUpdatedAt()).isNotNull();
        });
    }

    @Test
    void returnsAllTicketsAndTicketById() throws Exception {
        Long ticketId = createTicket();

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(ticketId));

        mockMvc.perform(get("/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ticketId))
                .andExpect(jsonPath("$.data.status").value("OPEN"));
    }

    @Test
    void updatesTicketStatus() throws Exception {
        Long ticketId = createTicket();

        mockMvc.perform(put("/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status": "CLOSED"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CLOSED"));

        Ticket updatedTicket = ticketRepository.findById(ticketId).orElseThrow();
        assertThat(updatedTicket.getStatus()).isEqualTo(TicketStatus.CLOSED);
    }

    @Test
    void returnsNotFoundForMissingTicket() throws Exception {
        mockMvc.perform(get("/tickets/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Ticket with id 999 was not found"))
                .andExpect(jsonPath("$.path").value("/tickets/999"));

        mockMvc.perform(put("/tickets/{id}/status", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status": "CLOSED"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void rejectsInvalidCreateRequest() throws Exception {
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": " ",
                                  "description": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.violations", hasSize(2)));

        assertThat(ticketRepository.count()).isZero();
    }

    @Test
    void rejectsMissingAndUnknownStatus() throws Exception {
        Long ticketId = createTicket();

        mockMvc.perform(put("/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status": null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.violations[0].field").value("status"));

        mockMvc.perform(put("/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status": "UNKNOWN"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed request body"));
    }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder validCreateRequest() {
        return post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Unable to sign in",
                          "description": "Valid credentials are rejected"
                        }
                        """);
    }

    private Long createTicket() throws Exception {
        mockMvc.perform(validCreateRequest()).andExpect(status().isCreated());
        return ticketRepository.findAll().get(0).getId();
    }
}
