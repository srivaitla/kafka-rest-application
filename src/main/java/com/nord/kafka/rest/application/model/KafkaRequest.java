package com.nord.kafka.rest.application.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KafkaRequest {

    private long id;
    private String type;

}
