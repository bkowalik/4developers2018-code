#!/bin/bash

TOPIC_NAME=$1
BOOTSTRAP_SERVER='localhost:9092'
OPS='--from-beginning'

docker exec -it 4developers2018_kafka_1 sh -c "exec /opt/kafka/bin/kafka-console-consumer.sh --topic $TOPIC_NAME --bootstrap-server $BOOTSTRAP_SERVER $OPS"
