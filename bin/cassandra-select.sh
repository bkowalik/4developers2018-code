#!/bin/bash

KEYSPACE='fordevelopers'
TABLE_NAME='dummy_data'

QUERY="SELECT * FROM $KEYSPACE.$TABLE_NAME;"

docker exec -it 4developers2018_cassandra_1 sh -c "cqlsh -e \"$QUERY\""
