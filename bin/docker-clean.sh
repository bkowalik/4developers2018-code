#!/bin/bash

CONTAINERS='4developers2018_zookeeper_1 4developers2018_kafka_1 4developers2018_cassandra_1'

docker kill $CONTAINERS 2>/dev/null 1>/dev/null || true
docker rm $CONTAINERS 2>/dev/null 1>/dev/null || true
yes | docker volume prune 2>/dev/null 1>/dev/null || true
