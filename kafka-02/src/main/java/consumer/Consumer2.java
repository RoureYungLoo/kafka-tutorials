package consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Consumer2 {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.101.134:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group-01");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

        List<String> topicList = new ArrayList<>();
        topicList.add("first");
        topicList.add("topic1");
        kafkaConsumer.subscribe(topicList);

        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(100));
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String topic = record.topic();
                    String value = record.value();
                    if (value != null && !value.isEmpty()) {
                        System.out.println(String.format("topic: %s, key: %s, value: %s", topic, key, value));
                    }
                }
            }
        } finally {
            // 同步提交
            kafkaConsumer.commitSync();
            // 异步提交
            kafkaConsumer.commitAsync();
            kafkaConsumer.close();
        }
    }
}
