# Lab2 3.10.2023

## Create initial topic

```yaml
/usr/bin/kafka-topics --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 3 --topic events1
```

## Create topic from outside of kafa container

```yaml
docker exec -ti kafka /usr/bin/kafka-topics --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 4 --topic events2
```

## How to see the topics

```yaml
/usr/bin/kafka-topics --list --bootstrap-server kafka:9092
```

## How to delete topic

```yaml
/usr/bin/kafka-topics --delete --bootstrap-server kafka:9092 --topic events2
```

## How to see the topic details

```yaml
/usr/bin/kafka-topics --describe --bootstrap-server kafka:9092 --topic events1
```

## How to create a producer

```yaml
/usr/bin/kafka-console-producer --broker-list kafka:9092 --topic events1
```

## How to create a consumer

- from kafka container

```yaml
/usr/bin/kafka-console-consumer --bootstrap-server kafka:9092 --topic events1 --from-beginning
```

- from outside of kafka container

```yaml
docker exec -ti kafka /usr/bin/kafka-console-consumer --bootstrap-server kafka:9092 --topic events1 --from-beginning
```

## Create a topic with 3 brokers and replication factor 2

```yaml
/usr/bin/kafka-topics --create --bootstrap-server kafka:9092 --replication-factor 2 --partitions 4 --topic events2
```

## Create a topic with 3 brokers and replication factor 3

```yaml
/usr/bin/kafka-topics --create --bootstrap-server kafka:9092 --replication-factor 3 --partitions 4 --topic events3
```

## Producer with key

```yaml
/usr/bin/kafka-console-producer --broker-list kafka:9092 --topic events1 --property parse.key="true" --property key.separator=","
```

## Consumer with key

```yaml
/usr/bin/kafka-console-consumer --bootstrap-server kafka:9092 --topic events1 --from-beginning --property print.key="true" --property key.separator=","
```

- from outside kafka container:

```yaml
docker exec -ti kafka /usr/bin/kafka-console-consumer --bootstrap-server kafka:9092 --topic events1 --from-beginning --property print.key="true" --property key.separator=","
```

## Set retention time for topic

```yaml
/usr/bin/kafka-configs --bootstrap-server kafka:9092 --entity-type topics --entity-name events1 --alter --add-config retention.ms=10000
```
