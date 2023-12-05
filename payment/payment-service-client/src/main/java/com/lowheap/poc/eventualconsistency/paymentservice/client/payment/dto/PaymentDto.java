package com.lowheap.poc.eventualconsistency.paymentservice.client.payment.dto;

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
public class PaymentDto implements Serializable {
    @JsonAlias({"id", "_id"})
    private String id;
    private PayeeDto payee;
    private PayerDto payer;
    private Double amount;
    private Boolean authorized;

}
