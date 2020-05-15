# ECONOMY API

## Run spring-boot

```
$ mvn spring-boot:run
```

## Swagger access

http://localhost:8080/v2/api-docs

## DATABASE

### H2 console

http://localhost:8080/h2-console

## HEROKU

```
$ heroku pg:psql
```

```
$ heroku pg:info
```


## KAFKA

Within Kafka folder

### CREATE TOPICS

```
$ kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic operation-topic
```

```
$ kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic bank-balance --config cleanup.policy=compact
```

### DELETE TOPICS

```
$ kafka-topics --delete --zookeeper localhost:2181 --topic operation-topic
```

```
$ kafka-topics --delete --zookeeper localhost:2181 --topic bank-balance
```

### CONSUMERS

```
$ kafka-console-consumer --bootstrap-server localhost:9092 \
    --topic operation-topic \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.LongDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
```