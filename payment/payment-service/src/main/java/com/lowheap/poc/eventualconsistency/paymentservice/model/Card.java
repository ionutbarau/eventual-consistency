package com.lowheap.poc.eventualconsistency.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String number;
    private String holderName;
    private Integer cvv;
    private LocalDate validThru;
    private Long balance;
}
