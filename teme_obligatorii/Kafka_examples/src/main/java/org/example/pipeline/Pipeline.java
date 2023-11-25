package org.example.pipeline;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.consumer.SimpleConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Pipeline {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleConsumer.class);

    private static final String OUR_CONSUMER_GROUP_ID = "group_1";
    private static final String OFFSET_RESET = "earliest";
    // : binds to any ip available
    private static final String BOOTSTRAP_SERVERS = "192.168.1.186:9094";
    private static final String CLIENT_ID = "producer_api";

    private static KafkaConsumer<String, String> kafkaConsumer;
    static KafkaProducer<String, String> kafkaProducer;

    private static Properties getConsumerKafkaProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, OUR_CONSUMER_GROUP_ID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_RESET);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        return props;
    }

    private static Properties getKafkaProducerProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,1048576);

        return props;
    }

    public static void pollKafka(String topic) {
        kafkaConsumer.subscribe(Collections.singleton(topic));

        try {
            Duration pollingTime = Duration.of(5, ChronoUnit.SECONDS);
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(pollingTime);
                // consume the records
                records.forEach(crtRecord -> {
                    LOG.info("------ PIPELINE CONSUMER ------------- topic ={}  key = {}, value = {} => partition = {}, offset = {}",
                            topic, crtRecord.key(), crtRecord.value(), crtRecord.partition(), crtRecord.offset());

                    // consumer becomes producer
                    ProducerRecord<String, String> prodRecord = new ProducerRecord<String, String>("da", crtRecord.value());
                    try {
                        RecordMetadata meta = kafkaProducer.send(prodRecord).get();
                        LOG.info("------ PIPELINE PRODUCER -------------key = {}, value = {} ==> partition = {}, offset = {}", prodRecord.key(), prodRecord.value(), meta.partition(), meta.offset());
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException | ExecutionException e)
                    {
                        //e.printStackTrace();
                    }
                });
            }
        }
        catch(WakeupException e)
        {
            LOG.info("Graceful shutdown of consumer");
        }
    }

    public static void main(String[] args) {
        kafkaConsumer = new KafkaConsumer<>(getConsumerKafkaProperties());
        kafkaProducer = new KafkaProducer<>(getKafkaProducerProperties());
        pollKafka("event");
    }
}
