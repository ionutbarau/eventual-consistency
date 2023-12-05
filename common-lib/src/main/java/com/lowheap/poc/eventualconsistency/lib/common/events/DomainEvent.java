package com.lowheap.poc.eventualconsistency.lib.common.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


@Data
@NoArgsConstructor
@SuperBuilder
public abstract class DomainEvent implements Serializable {

    private static final long serialVersionUID = 4945887390590304513L;
    @Id
    private String id;
    private String aggregateType;
    private String aggregateId;
    private String type;
}
