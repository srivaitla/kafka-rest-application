package com.nord.kafka.rest.application.producer;

import com.nord.kafka.rest.application.dto.KafkaRequest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaStringProducer {

    private static final Logger LOGGER = LogManager.getLogger(KafkaStringProducer.class);

    private final String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaStringProducer(@Value("${kafka.topic.name.string}") String topicName, KafkaTemplate<String, String> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishToTopic(KafkaRequest request, String sessionId) {
        final ProducerRecord<String, String> record = new ProducerRecord<>(topicName, null, sessionId, request.toString(), buildHeaderRecords());
        LOGGER.info("KafkaProducer-String ----- ----- ----- ----- ----- ----- Started\n");
        LOGGER.info(record.toString() + "\n");
        try {
            kafkaTemplate.send(record);
            LOGGER.info("KafkaProducer-String ----- ----- ----- Completed\n\n\n");
        } catch (Exception ex) {
            LOGGER.info("KafkaProducer-String ----- ----- ----- Exception : " + ex.getMessage() + "\n\n\n");
            throw ex;
        }
    }

    private List<Header> buildHeaderRecords() {
        final Header header1 = new RecordHeader("header-key-1", "String-value-1".getBytes(StandardCharsets.UTF_8));
        final Header header2 = new RecordHeader("header-key-2", "String-value-2".getBytes(StandardCharsets.UTF_8));

        final List<Header> producerHeaders = new ArrayList<>();
        producerHeaders.add(header1);
        producerHeaders.add(header2);
        return producerHeaders;
    }

}
