package com.nord.kafka.rest.application.processor;

import com.nord.kafka.rest.application.dto.KafkaRequest;
import com.nord.kafka.rest.application.producer.KafkaAVROProducer;
import com.nord.kafka.rest.application.producer.KafkaStringProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaAVROProcessor {

    private static final Logger LOGGER = LogManager.getLogger(KafkaAVROProcessor.class);

    @Autowired
    private KafkaAVROProducer producer;

    public void process(KafkaRequest request, String sessionId) {
        LOGGER.info("KafkaProcessor-AVRO ----- ----- ----- Started\n");

        producer.publishToTopic(request, sessionId);

        LOGGER.info("KafkaProcessor-AVRO ----- ----- ----- Completed\n");
    }
}
