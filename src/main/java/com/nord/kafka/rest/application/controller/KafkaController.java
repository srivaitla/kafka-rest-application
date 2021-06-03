package com.nord.kafka.rest.application.controller;

import com.nord.kafka.rest.application.model.KafkaRequest;
import com.nord.kafka.rest.application.model.KafkaResponse;
import com.nord.kafka.rest.application.processor.KafkaProcessor;
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
public class KafkaController {

    private static final Logger LOGGER = LogManager.getLogger(KafkaController.class);

    @Autowired
    private KafkaProcessor processor;

    @PostMapping(value = "/kafka/produce",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public KafkaResponse produceToTopic(@RequestBody KafkaRequest request) {
        LOGGER.info("KafkaController ----- ----- ----- Started\n");
        LOGGER.info(request + "\n");
        final String sessionId = UUID.randomUUID().toString();

        try {
            processor.process(request, sessionId);
        } catch (Exception ex) {
            LOGGER.info("KafkaController ----- ----- ----- Exception : " + Arrays.toString(ex.getStackTrace()) + "\n\n\n");
            return buildResponse(request, sessionId, "Failed: " + ex.getMessage());
        }

        final KafkaResponse response = buildResponse(request, sessionId, "Success");
        LOGGER.info(response + "\n");
        LOGGER.info("KafkaController ----- ----- ----- Completed\n\n\n");
        return response;
    }

    private KafkaResponse buildResponse(KafkaRequest request, String sessionId, String result) {
        final KafkaResponse response = new KafkaResponse();
        response.setRequestId(request.getId());
        response.setSessionId(sessionId);
        response.setResult(result);
        return response;
    }
}
