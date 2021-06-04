package com.nord.kafka.rest.application.controller;

import com.nord.kafka.rest.application.dto.KafkaRequest;
import com.nord.kafka.rest.application.dto.KafkaResponse;
import com.nord.kafka.rest.application.processor.KafkaAVROProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.UUID;

@RestController
public class KafkaAVROController {

    private static final Logger LOGGER = LogManager.getLogger(KafkaAVROController.class);

    @Autowired
    private KafkaAVROProcessor processor;

    @PostMapping(value = "/kafka/produce/avro",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String produceToTopic(@RequestBody KafkaRequest request) {
        LOGGER.info("KafkaController-AVRO ----- ----- ----- Started\n");
        LOGGER.info(request + "\n");
        final String sessionId = UUID.randomUUID().toString();

        try {
            processor.process(request, sessionId);
        } catch (Exception ex) {
            LOGGER.info("KafkaController-AVRO ----- ----- ----- Exception : " + ExceptionUtils.getStackTrace(ex) + "\n\n\n");
            return buildResponse(request, sessionId, "Failed: " + ex.getMessage()).toString();
        }

        final KafkaResponse response = buildResponse(request, sessionId, "Success");
        LOGGER.info(response.toString() + "\n");
        LOGGER.info("KafkaController-AVRO ----- ----- ----- Completed\n\n\n");
        return response.toString();
    }

    private KafkaResponse buildResponse(KafkaRequest request, String sessionId, String result) {
        final KafkaResponse response = new KafkaResponse();
        response.setId(request.getId());
        response.setSessionId(sessionId);
        response.setResult(result);
        return response;
    }
}
