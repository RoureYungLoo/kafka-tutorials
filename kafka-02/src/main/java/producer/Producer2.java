package producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import partition.MyPartitioner;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Producer2 {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.101.134:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, 1);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.GZIP.name);

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        String topic = "topic1";
        String key = "";
        String value = "value";
        for (int i = 0; i < 10; i++) {
            key = key + i;
            value = value + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            // kafkaProducer.send(record); // 忽略发送结果
            // kafkaProducer.send(record).get(); // 同步发送
            kafkaProducer.send(record, new MyCallback()); // 异步发送
            TimeUnit.MILLISECONDS.sleep(500L);
        }
        kafkaProducer.close();

    }

    static class MyCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (exception != null) {
                System.out.println("发送失败");
            }
            String topic = metadata.topic();
            int partition = metadata.partition();
            long offset = metadata.offset();
            System.out.println(String.format("发送成功: %s %d %d", topic, partition, offset));
        }
    }
}

