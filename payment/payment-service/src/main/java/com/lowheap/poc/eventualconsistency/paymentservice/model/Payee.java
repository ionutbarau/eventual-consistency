package com.lowheap.poc.eventualconsistency.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payee {
    private String name;
    private String bankAccount;
    private String bankName;
}
