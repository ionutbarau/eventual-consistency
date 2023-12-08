package com.lowheap.poc.eventualconsistency.paymentservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private Card card;
    private String status;
    private LocalDateTime createdAt;
}
