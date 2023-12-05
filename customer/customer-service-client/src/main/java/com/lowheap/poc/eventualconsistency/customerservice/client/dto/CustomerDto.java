package com.lowheap.poc.eventualconsistency.customerservice.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto implements Serializable {
    private static final long serialVersionUID = 4151139079742814530L;
    @JsonAlias({"id", "_id"})
    private String id;
    private String firstName;
    private String lastName;
    private CardDto card;
    private RegistrationStatus status;
}
