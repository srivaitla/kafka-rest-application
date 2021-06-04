package com.nord.kafka.rest.application.producer;

import com.nord.kafka.rest.application.dto.KafkaRequest;
import org.apache.avro.specific.SpecificRecordBase;
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
public class KafkaAVROProducer {

    private static final Logger LOGGER = LogManager.getLogger(KafkaAVROProducer.class);

    private final String topicName;
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    @Autowired
    public KafkaAVROProducer(@Value("${kafka.topic.name.avro}") String topicName, KafkaTemplate<String, SpecificRecordBase> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishToTopic(KafkaRequest request, String sessionId) {
        final ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topicName, null, sessionId, request, buildHeaderRecords());
        LOGGER.info("KafkaProducer-AVRO ----- ----- ----- ----- ----- ----- Started\n");
        LOGGER.info(record.toString() + "\n");
        try {
            kafkaTemplate.send(record);
            LOGGER.info("KafkaProducer-AVRO ----- ----- ----- Completed\n\n\n");
        } catch (Exception ex) {
            LOGGER.info("KafkaProducer-AVRO ----- ----- ----- Exception : " + ex.getMessage() + "\n\n\n");
            throw ex;
        }
    }

    private List<Header> buildHeaderRecords() {
        final Header header1 = new RecordHeader("header-key-1", "AVRO-value-1".getBytes(StandardCharsets.UTF_8));
        final Header header2 = new RecordHeader("header-key-2", "AVRO-value-2".getBytes(StandardCharsets.UTF_8));

        final List<Header> producerHeaders = new ArrayList<>();
        producerHeaders.add(header1);
        producerHeaders.add(header2);
        return producerHeaders;
    }

}
