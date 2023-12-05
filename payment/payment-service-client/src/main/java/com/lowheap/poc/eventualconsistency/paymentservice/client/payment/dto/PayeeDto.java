package com.lowheap.poc.eventualconsistency.paymentservice.client.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayeeDto {
    private String name;
    private String bankAccount;
    private String bankName;
}
