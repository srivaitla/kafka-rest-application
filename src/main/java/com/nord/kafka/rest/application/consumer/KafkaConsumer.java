package com.nord.kafka.rest.application.consumer;

import com.nord.kafka.rest.application.util.KafkaUtility;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @Autowired
    private KafkaUtility utility;

    private static final Logger LOGGER = LogManager.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTopic(ConsumerRecord<String, String> record) {
        LOGGER.info("KafkaConsumer ----- ----- ----- Started\n");
        LOGGER.info(record.toString() + "\n");
        LOGGER.info("Headers=" + utility.getHeadersAsString(record.headers()) + "\n");
        LOGGER.info("KafkaConsumer ----- ----- ----- Completed\n\n\n");
    }
}
