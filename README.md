# Kafka Rest Application
- It's Spring boot REST service.
- It's a Kafka service has both kafka producer and consumer services, which consumes and produces '**String**' data to/from kafka.

- It provides rest endpoint to receive the Kafka request.
- It converts kafka object into String format and handover to its Producer (`com.nord.kafka.rest.service.producer.KafkaProducer`).
- Its producer publishes this string data to 'kafka topic'.
- Its consumer (`com.nord.kafka.rest.service.consumer.KafkaConsumer`) consumes this string data from 'kafka topic'.
- It converts string data back to kafka request object and logs the details.
