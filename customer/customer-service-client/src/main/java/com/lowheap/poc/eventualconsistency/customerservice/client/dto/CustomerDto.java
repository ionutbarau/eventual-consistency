package com.lowheap.poc.eventualconsistency.customerservice.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lowheap.poc.eventualconsistency.lib.common.serializers.DebeziumLocalDateDeserializer;
import com.lowheap.poc.eventualconsistency.lib.common.serializers.DebeziumLocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @JsonDeserialize(using = DebeziumLocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
}
