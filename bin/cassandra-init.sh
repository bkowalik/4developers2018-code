#!/bin/bash

KEYSPACE='fordevelopers'
TABLE_NAME='dummy_data'

QUERY="CREATE KEYSPACE IF NOT EXISTS $KEYSPACE WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};"
QUERY=$QUERY"CREATE TABLE IF NOT EXISTS $KEYSPACE.$TABLE_NAME (id UUID PRIMARY KEY, content text, created timestamp);"

docker exec -it 4developers2018_cassandra_1 sh -c "cqlsh -e \"$QUERY\""
