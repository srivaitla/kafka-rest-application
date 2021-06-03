package com.nord.kafka.rest.application.processor;

import com.nord.kafka.rest.application.model.KafkaRequest;
import com.nord.kafka.rest.application.producer.KafkaProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaProcessor {

    private static final Logger LOGGER = LogManager.getLogger(KafkaProcessor.class);

    @Autowired
    private KafkaProducer producer;

    public void process(KafkaRequest request, String sessionId) {
        LOGGER.info("KafkaProcessor ----- ----- ----- Started\n");

        producer.publishToTopic(request, sessionId);

        LOGGER.info("KafkaProcessor ----- ----- ----- Completed\n");
    }
}
