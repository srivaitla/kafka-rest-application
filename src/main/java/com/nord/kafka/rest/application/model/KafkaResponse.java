package com.nord.kafka.rest.application.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KafkaResponse {

    private long requestId;
    private String sessionId;
    private String result;

}
