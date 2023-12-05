package com.lowheap.poc.eventualconsistency.paymentservice.client.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayerDto {
    private String name;
    private CardDto card;
}
