package com.nord.kafka.rest.application.util;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class KafkaUtility {

    public String getHeadersAsString(Headers recordHeaders) {
        final StringBuilder headersBuilder = new StringBuilder().append('[');
        for (Header header : recordHeaders) {
            headersBuilder.append(header.key()).append('=').append(new String(header.value(), StandardCharsets.UTF_8)).append(';');
        }
        headersBuilder.append(']');
        return headersBuilder.toString();
    }
}
