FROM confluentinc/cp-kafka-connect:latest

RUN confluent-hub install --no-prompt --verbose mongodb/kafka-connect-mongodb:latest