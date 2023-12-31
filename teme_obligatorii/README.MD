# Paths to each exercise 

## 1. Creare Producer Kafka din consola (terminal).
    --> lab2 folder / lab2_commands.MD
## 2. Creare Consumer Kafka din consola (terminal).
    --> lab2 folder / lab2_commands.MD
## 3. Creare ProducerAPI care trimite evenimente sub forma de siruri de cacactere pe Kafka.
    ---> Kafka_examples/src/main/java/org/example/producer/SimpleSyncProducer.java
## 4. Creare ConsumerAPI Kafka ptr Producer-ul de mai sus.
    --> Kafka_examples/src/main/java/org/example/consumer/SimpleConsumer.java
## 5. Creare ProducerAPI care trimite evenimente sub forma json pe Kafka.
    --> Kafka_examples/src/main/java/org/example/json/JsonStringProducer.java
## 6. Creare ConsumerAPI Kafka ptr Producer-ul de mai sus.
    --> Kafka_examples/src/main/java/org/example/json/JsonStringConsumer.java
## 5. Creare ProducerAPI care trimite evenimente sub forma de obiecte definite (clase Java, C#, etc) in API pe Kafka.
    --> Kafka_examples/src/main/java/org/example/json/JsonObjectProducer.java
## 6. Creare ConsumerAPI Kafka ptr Producer-ul de mai sus.
    --> Kafka_examples/src/main/java/org/example/json/JsonObjectConsumer.java 
## 7. Creare ProducerAPI si publicare mesaje folosind schema for Kafka.
    --> Kafka_examples/src/main/java/org/example/avro/AvroProducer.java
## 8. Creare ConsumerAPI for Kafka si consumare mesaje folosind schema.
    --> Kafka_examples/src/main/java/org/example/avro/AvroConsumer.java
## 9. Creare pipeline: un ConsumerAPI poate deveni mai departe Producator.
    --> Kafka_examples/src/main/java/org/example/pipeline/Pipeline.java
## 10. Folositi: Confluent REST proxy si prin cereri HTTP: creati un topic, cititi mesajele din topic.

![Ex_10](D:\GithubCloning\MQT\teme_obligatorii\resources\ex_10.png)
## 11. Creare API care publica evenimente pe 2 topic-uri Kafka. Folosind Kafka Streams, agregati datele de pe cele 2 topic-uri in alt topic.
    --> Pentru produceri am folosit: kafka_examples/src/main/java/org/example/producer/SimpleSyncProducer.java
    --> Kafka_examples/src/main/java/org/example/kafkastreams/KafkaAggregate.java
## 12. Creare API care publica evenimente pe 2 topic-uri Kafka. Folosind kSQL efectuati cereri SQL folosind datele din cele 2 topic-uri.
    CREATE STREAM stream1 (id INT, name STRING) WITH (KAFKA_TOPIC='my-first-topic', VALUE_FORMAT='AVRO');
    CREATE STREAM stream2 (id INT, description STRING) WITH (KAFKA_TOPIC='my-second-topic', VALUE_FORMAT='AVRO');
    
    SELECT stream1.id, stream1.name, stream2.description
    FROM stream1
    LEFT JOIN stream2 ON stream1.id = stream2.id;
## 13. Folosind un source Kafka connector cititi datele dintr-o tabele a unei baza de date (la alegere) si publicati-le pe un Kakfa topic.

## 14. Folosind un sink Kafka connector cititi datele dintr-un topic si publicati-le intr-o baza de date (la alegere).

## 15. Avand la dispozitie un fisier de mari dimensiuni, parsati-l folosind Spark RDDs: numarati de cate ori apare cuvantul "master" in fisier.
    --> teme_obligatorii/Spark
## 16. Avand la dispozitie un fisier de mari dimensiuni, parsati-l folosind Spark RDDs: afisati toate valorile numerice din fisier.
     --> teme_obligatorii/Spark
## 17. Avand la dispozitie doua fisiere de mari dimensiuni, folosind Spark RDDs parsati-l. Fisierul 1 contine cuvintele unui articol.
     --> teme_obligatorii/Spark
Fisierul 2 contine cuvinte uzuale, boring. In urma analizei celor 2 fisiere generati un rezultat, in forma de fisier,

care reprezinta cuvintele din primul fisier, fara prezenta cuvintelor din al doilea fisier.

Un fisier fara "boring" words. 