package com.lowheap.poc.eventualconsistency.customerservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String city;
    private String street;
    private Integer number;
    private String postalCode;
    private String geoLocation;
}
