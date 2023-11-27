package org.example.kafkstreams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class KafkaAggregate {
    public static void main(String[] args) {
        Properties config = getConfig();
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> stream1 = createStream(builder, "first-topic");
        KStream<String, String> stream2 = createStream(builder, "second-topic");

        KStream<String, String> aggregatedStream = aggregateStreams(stream1, stream2);

        publishToOutputTopic(aggregatedStream, "output-topic");

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), config);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    private static Properties getConfig() {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-example");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return config;
    }

    private static KStream<String, String> createStream(StreamsBuilder builder, String inputTopic) {
        return builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));
    }

    private static KStream<String, String> aggregateStreams(KStream<String, String> stream1, KStream<String, String> stream2) {
        return stream1.merge(stream2);
    }

    private static void publishToOutputTopic(KStream<String, String> stream, String outputTopic) {
        stream.to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));
    }
}
