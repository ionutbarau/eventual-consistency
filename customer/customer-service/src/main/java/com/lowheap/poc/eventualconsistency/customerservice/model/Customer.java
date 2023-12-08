package com.lowheap.poc.eventualconsistency.customerservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection= "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {
    private static final long serialVersionUID = 4151139079742814530L;
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Card card;
    private RegistrationStatus status;
    private LocalDateTime createdAt;
}
