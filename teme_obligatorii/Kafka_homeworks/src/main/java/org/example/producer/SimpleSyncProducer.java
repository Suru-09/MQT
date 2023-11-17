package org.example.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;


public class SimpleSyncProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSyncProducer.class);
    // : binds to any ip available
    private static final String BOOTSTRAP_SERVERS = "192.168.1.186:9094";
    private static final String CLIENT_ID = "producer_api";

    private static Producer<String, String> kafkaProducer;

    record Range(int min, int max) {}

    private static Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,1048576);

        return props;
    }

    private static int generateKeyForProducer(Range range)
    {
        return new Random().nextInt(range.max - range.min) + range.min;
    }

    private static String generateMessageForProducer(int stringSize)
    {
        byte[] array = new byte[stringSize];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    private static void sendRandomMessage(String topic) {
        String key = Integer.toString(generateKeyForProducer(new Range(0, 10)));
        String message = generateMessageForProducer(20);
        ProducerRecord<String, String> data = new ProducerRecord<>(topic, key, message );

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
