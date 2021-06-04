package com.nord.kafka.rest.application.processor;

import com.nord.kafka.rest.application.dto.KafkaRequest;
import com.nord.kafka.rest.application.producer.KafkaAVROProducer;
import com.nord.kafka.rest.application.producer.KafkaStringProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaStringProcessor {

    private static final Logger LOGGER = LogManager.getLogger(KafkaStringProcessor.class);

    @Autowired
    private KafkaStringProducer producer;

    public void process(KafkaRequest request, String sessionId) {
        LOGGER.info("KafkaProcessor-String ----- ----- ----- Started\n");

        producer.publishToTopic(request, sessionId);

        LOGGER.info("KafkaProcessor-String ----- ----- ----- Completed\n");
    }
}
