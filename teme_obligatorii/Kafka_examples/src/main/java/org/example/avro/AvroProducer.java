package org.example.avro;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.json.JCompany;
import org.example.producer.JsonObjectProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producer.Company;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;



public class AvroProducer {
    private static final Logger LOG = LoggerFactory.getLogger(JsonObjectProducer.class);
    // : binds to any ip available
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String CLIENT_ID = "producer_api";

    private static Producer<String, Company> kafkaProducer;
    public record Range(int min, int max) {}

    private static Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,1048576);
        props.put("schema.registry.url", "http://localhost:8081");
        return props;
    }

    private static int generateKeyForProducer(Range range)
    {
        return new Random().nextInt(range.max - range.min) + range.min;
    }

    private static Company generateMessageForProducer()
    {
        var number = generateKeyForProducer(new Range(10, 50));
        return new Company(number, "haha" + number);
    }

    private static void sendRandomMessage(String topic) {
        String key = Integer.toString(generateKeyForProducer(new Range(0, 10)));
        var message = generateMessageForProducer();
        ProducerRecord<String, Company> data = new ProducerRecord<>(topic, key, message);

        try {
            RecordMetadata meta = kafkaProducer.send(data).get();
            LOG.info("key = {}, value = {} ==> partition = {}, offset = {}", data.key(), data.value(), meta.partition(), meta.offset());
            Thread.sleep(5000);
        }
        catch (InterruptedException | ExecutionException e) {
            kafkaProducer.flush();
        }
    }

    public static void main(String[] args) {
        // initialize the producer
        kafkaProducer = new KafkaProducer<>(getKafkaProperties());
        int numberOfMessages = 100;
        IntStream.range(0, numberOfMessages).forEach(i -> {
            sendRandomMessage("event");
        });
    }
}
