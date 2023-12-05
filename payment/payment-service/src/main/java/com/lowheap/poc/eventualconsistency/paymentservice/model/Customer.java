package com.lowheap.poc.eventualconsistency.paymentservice.model;

import lombok.Data;

@Data
public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private Card card;
    private String status;
}
