# Kafka Rest Application
- It's Spring boot REST service.
- It's a Kafka service has both kafka producer and consumer services, which consumes and produces '**String**' data to/from kafka.

- It provides rest endpoint to receive the Kafka request.
- It converts kafka object into String format and handover to its Producer (`com.nord.kafka.rest.service.producer.KafkaProducer`).
- Its producer publishes this string data to 'kafka topic'.
- Its consumer (`com.nord.kafka.rest.service.consumer.KafkaConsumer`) consumes this string data from 'kafka topic'.
- It converts string data back to kafka request object and logs the details.


## Pre-Requisite:

- Install Zookeeper, Kafka and Schema Register in Local/Test. Steps are given in 'Kafka Setup' section.
- Register schemas which are under 'src/main/schema/avro/' folder for your topics (name is given in the application.properties file).



## ShortCuts of BashProfile to Start, Stop and Delete Kafka in Local:

        alias cdkafka='cd ~/Source/Tools/confluent-6.1.1'
        alias runzookeeper='./bin/zookeeper-server-start ./etc/kafka/zookeeper.properties'
        alias runkafka='./bin/kafka-server-start ./etc/kafka/server.properties'
        alias runschema='./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties'

        alias stopzookeeper='./bin/zookeeper-server-stop'
        alias stopkafka='./bin/kafka-server-stop'
        alias stopschema='./bin/schema-registry-stop'

        alias deletekafka='rm -rf /tmp/kafka-logs/'
        alias deletezookeeper='rm -rf /tmp/zookeeper/'


## Setup 'Kafka' in Local:

- Download the kafka setup from confluent (https://www.confluent.io/)
- Goto the folder where kafka setup is installed. Example: Goto '~/confluent-6.1.1/'

**1). Start ZooKeeper**

Run following the command to start the Zookeeper:

	./bin/zookeeper-server-start ./etc/kafka/zookeeper.properties


**2). Start Kafka Server:**

Before start the Kafka, we need to update 'listeners' entry with following value to 'confluent-6.1.1/etc/kafka/server.properties' file.

    listeners=PLAINTEXT://localhost:9092

Run following the command to start the Kafka:

	./bin/kafka-server-start ./etc/kafka/server.properties


**3). Run Schema Registry:**

Before run the Schema Registry, we need to update 'listeners' and 'kafkastore' entries with following values to 'confluent-6.1.1/etc/schema-registry/schema-registry.properties' file.

    listeners=http://localhost:8082
    kafkastore.connection.url=localhost:2181

Run following the command to start the Schema Registry:

	./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties


## Kafka Topic - Create/Delete/Describe:

**1). List of Kafka Topics:**

	./bin/kafka-topics --list --zookeeper localhost:2181

**2). Create Kafka Topic:**

	./bin/kafka-topics --create --bootstrap-server localhost:9092 --topic topic_name

	Example:
    ./bin/kafka-topics --create --bootstrap-server localhost:9092 --topic sri-test-string
    ./bin/kafka-topics --create --bootstrap-server localhost:9092 --topic sri-test-avro

**3). Describe a Topic:**

	./bin/kafka-topics --describe --bootstrap-server localhost:9092 --topic topic_name

    Example:
    ./bin/kafka-topics --describe --bootstrap-server localhost:9092 --topic sri-test-string
    ./bin/kafka-topics --describe --bootstrap-server localhost:9092 --topic sri-test-avro

**4). Deleting a Topic:**

	./bin/kafka-topics --zookeeper localhost:2181 --delete --topic topic_name

    Example:
    ./bin/kafka-topics --zookeeper localhost:2181 --delete --topic sri-test-string
    ./bin/kafka-topics --zookeeper localhost:2181 --delete --topic sri-test-avro


## Schema Register:

- Additional documentation guide:
- https://docs.huihoo.com/apache/kafka/confluent/3.2/schema-registry/docs/intro.html
- https://docs.confluent.io/platform/current/tutorials/examples/clients/docs/kafka-commands.html

**1). Get the list of schemas - Including schema details:**


	curl -i -X GET http://localhost:8082/schemas

    REST Endpoint:
    http://localhost:8082/schemas


**2). Get the list of schemas - subjects only:**


    curl -i -X GET http://localhost:8082/subjects

	REST Endpoint:
	http://localhost:8082/subjects


**3). Fetch a schema by globally unique ID:**

	curl -i -X GET http://localhost:8082/schemas/ids/id_number

	(where id_number is the schema number found using 'curl -i -X GET http://localhost:8082/subjects')

	Example:
	curl -i -X GET http://localhost:8082/schemas/ids/1


**4). List all schema versions of the schema registered under subject "topic_name-value":**

	curl -i -X GET http://localhost:8082/subjects/topic_name-value/versions

	Example:
	curl -i -X GET http://localhost:8082/subjects/sri-test-string-value/versions
    curl -i -X GET http://localhost:8082/subjects/sri-test-avro-value/versions


**5). Fetch the specific version of the schema registered under subject "topic_name-value":**

	curl -i -X GET http://localhost:8082/subjects/topic_name-value/versions/version_number

	(version_number can be get using all schema versions command)

	Example:
	curl -i -X GET http://localhost:8082/subjects/sri-test-string-value/versions/1
    curl -i -X GET http://localhost:8082/subjects/sri-test-avro-value/versions/1


**6). Soft delete a schema:**

a). Delete all schema versions registered under the subject "topic_name-value".


  	curl -i -X DELETE http://localhost:8082/subjects/topic_name-value

    Example:
    curl -i -X DELETE http://localhost:8082/subjects/sri-test-string-value
    curl -i -X DELETE http://localhost:8082/subjects/sri-test-avro-value


b). Delete all schema versions registered under the subject "topic_name-value" using userName and password for Test env.


    curl -i -X DELETE -H "Content-Type: application/vnd.schemaregistry.v1+json" -u "userName:password" https://test_evn_url/subjects/topic_name-value

    Here, we need to provide userName, password, test_evn_url and topic_name details for Test env.


c). Delete the specific version of the schema registered under subject "topic_name-value".


  	curl -i -X DELETE http://localhost:8082/subjects/topic_name-value/versions/1

    Example:
    curl -i -X DELETE http://localhost:8082/subjects/sri-test-string-value/versions/1
    curl -i -X DELETE http://localhost:8082/subjects/sri-test-avro-value/versions/1


d). Delete the most recent version of the schema registered under subject "topic_name-value".


    curl -i -X DELETE http://localhost:8082/subjects/topic_name-value/versions/latest

    Example:
    curl -i -X DELETE http://localhost:8082/subjects/sri-test-string-value/versions/latest
    curl -i -X DELETE http://localhost:8082/subjects/sri-test-avro-value/versions/latest


**7). Hard delete a schema:**

Hard delete of a schema with the use of the query string, '?permanent=true' for above commands.


	curl -i -X DELETE http://localhost:8082/subjects/topic_name-value?permanent=true

	Example:
	curl -i -X DELETE http://localhost:8082/subjects/sri-test-string-value?permanent=true
	curl -i -X DELETE http://localhost:8082/subjects/sri-test-avro-value?permanent=true


**8). List of supported schema types:**

	curl -i -X GET http://localhost:8082/schemas/types


**9). Register a new version of a schema (String format for key) under the subject "topic_name-key":**

	curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\": \"string\"}"}' http://localhost:8082/subjects/topic_name-key/versions

	Example:
	curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\": \"string\"}"}' http://localhost:8082/subjects/sri-test-string-key/versions


**10).Check if a schema Is registered under subject topic_name-key"**

	curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\": \"string\"}"}' http://localhost:8082/subjects/topic_name-key

	Example:
	curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\": \"string\"}"}' http://localhost:8082/subjects/sri-test-string-key


**11). Update compatibility requirements globally for "topic_name-key":**

	curl -i -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"compatibility": "FULL"}' http://localhost:8082/config/topic_name-key

	Example:
	curl -i -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"compatibility": "FULL"}' http://localhost:8082/config/sri-test-string-key


**12). Register a new version of a schema (String format for value) under the subject "topic_name-value"**

	curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\": \"string\"}"}' http://localhost:8082/subjects/topic_name-value/versions

	Example:
	curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\": \"string\"}"}' http://localhost:8082/subjects/sri-test-string-value/versions


**13). Register a new version of a schema (AVRO format for value) under the subject "topic_name-value"**

a). Using schema from a file as part of 'jq' software:

  -   Install 'jq' software from here : https://stedolan.github.io/jq/download/
  -   Install 'jq' software using Homebrew: "brew install jq"
  -   Use following command to get the schema from file and register to schema registry.
  -   Please note, path for the avsc file and schema name need to change based on the requirement:


    jq '. | {schema: tojson}' path_to_avsc_file_contains_schema | curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" http://localhost:8082/subjects/topic_name-value/versions -d @-

    Example:
    jq '. | {schema: tojson}' ~/Source/git/git_learn/kafka-rest-application/schema/avro/kafka-request.avsc | curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" http://localhost:8082/subjects/sri-test-avro-value/versions -d @-
    jq '. | {schema: tojson}' ~/Source/git/git_learn/kafka-rest-application/schema/avro/kafka-response.avsc | curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" http://localhost:8082/subjects/sri-test-avro_Response-value/versions -d @-


  -   For test environment, if we have userName, password, schema_registry_URL and topic_name, we need to use following command:

    jq '. | {schema: tojson}' path_to_avsc_file_contains_schema | curl -i -u "userName:password" -H "Content-Type: application/vnd.schemaregistry.v1+json" -X POST https://schema_registry_URL/subjects/topic_name-value/versions -d @-


b). Using entire schema as part of command:


	Example:
	curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\":\"record\",\"name\":\"KafkaRequest\",\"namespace\":\"com.nord.kafka.rest.application.dto\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"message\",\"type\":\"string\"}]}"}' http://localhost:8082/subjects/sri-test-avro-value/versions

	curl -i -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\":\"record\",\"name\":\"KafkaResponse\",\"namespace\":\"com.nord.kafka.rest.application.dto\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"sessionId\",\"type\":\"string\"},{\"name\":\"result\",\"type\":\"string\"}]}"}' http://localhost:8082/subjects/sri-test-avro_Response-value/versions


**14). Update compatibility requirements globally for "topic_name-value":**

	curl -i -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"compatibility": "FULL"}' http://localhost:8082/config/topic_name-value

	Example:
	curl -i -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"compatibility": "FULL"}' http://localhost:8082/config/sri-test-string-value
	curl -i -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"compatibility": "FULL"}' http://localhost:8082/config/sri-test-avro-value


## Kafka Produce and Consume - String Messages:

- To Produce or Consumer the '**String**' messages/events to/from the kafka topic, we don't need to register the schema.

**1). Produce (String) message to Kafka Topic:**

a). Run below command to start Producer:


	./bin/kafka-console-producer --bootstrap-server localhost:9092 --topic topic_name

    Example:
	./bin/kafka-console-producer --bootstrap-server localhost:9092 --topic sri-test-string

b). Enter Following messages in the producer console:


    abc
    xyz


**2). Consumer (String) message from Kafka Topic:**

	./bin/kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --topic topic_name
	
    Eaxmple:	
	./bin/kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --topic sri-test-string


## Kafka Produce and Consume - AVRO Messages:

- To Produce or Consumer the '**AVRO**' messages/events to/from the kafka topic, we have to register the schema.

**1). Produce (AVRO) message to Kafka Topic using existing schema id:**

a). Run below command to start Producer:


	./bin/kafka-avro-console-producer --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8082 --property value.schema.id=schema_Id --topic topic_name

    Example:
    ./bin/kafka-avro-console-producer --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8082 --property value.schema.id=2 --topic sri-test-avro

b). Enter Following messages in the producer console:


    {"id":1, "message":"produce1"}
    {"id":2, "message":"produce2"}


**2). Produce (AVRO) message to Kafka Topic using schema specified along with command line:**

	./bin/kafka-avro-console-producer --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8082 --property value.schema='Specify_schema_here' --topic topic_name

    Example:
    ./bin/kafka-avro-console-producer --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8082 --property value.schema='{"type":"record","name":"KafkaRequest","namespace":"com.nord.kafka.rest.application.dto","fields":[{"name":"id","type":"long"},{"name":"message","type":"string"}]}' --topic sri-test-avro

**3). Consumer (AVRO) message from Kafka Topic:**

	./bin/kafka-avro-console-consumer --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8082 --from-beginning --topic topic_name

    Example:
    ./bin/kafka-avro-console-consumer --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8082 --from-beginning --topic sri-test-avro


## Regular Topic - Cosumer Lag:

- To view and reset consumer lag of the topic.
- Before that create following configuration in ssl_config file if Kafka schema using SSL.
 
	request.timeout.ms = 30000
	security.protocol = SASL_SSL
	sasl.mechanism = SCRAM-SHA-512
	sasl.jaas.config = org.apache.kafka.common.security.scram.ScramLoginModule required username="<user_name>" password="<password>";

**1). Describe:**

	bin/kafka-consumer-groups.sh --bootstrap-server <serverName:port_number> --group <topic_group_id> --describe --command-config <ssl_config name with path>

**2). Reset Lag to Latest:**

	bin/kafka-consumer-groups.sh --bootstrap-server <serverName:port_number> --group <topic_group_id> --reset-offsets --to-latest --topic <topic_name> --execute --command-config <ssl_config name with path>

## ChangeLog Topic:

- To view and set update to change log topic
	
**1). Describe:**

	bin/kafka-topics.sh --describe --bootstrap-server <serverName:port_number> --command-config <ssl_config name with path>
 --topic <changelog_topic_name>

**2). Set config:**

	bin/kafka-configs.sh --bootstrap-server <serverName:port_number> --command-config <ssl_config name with path> --alter --entity-type topics --entity-name <changelog_topic_name> --add-config max.message.bytes=<bytes_value>



	
