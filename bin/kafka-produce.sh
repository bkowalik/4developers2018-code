#!/bin/bash

TOPIC_NAME=$1
BROKER_LIST='localhost:9092'

docker exec -it 4developers2018_kafka_1 sh -c "exec /opt/kafka/bin/kafka-console-producer.sh --topic $TOPIC_NAME --broker-list $BROKER_LIST"
