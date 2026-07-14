package com.example.supportticket.mapper;

import com.example.supportticket.domain.dto.request.CreateTicketRequest;
import com.example.supportticket.domain.dto.response.TicketResponse;
import com.example.supportticket.domain.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TicketMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "OPEN")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Ticket toEntity(CreateTicketRequest request);

    TicketResponse toResponse(Ticket ticket);

    List<TicketResponse> toResponses(List<Ticket> tickets);
}
