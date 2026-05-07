package producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.serialization.StringSerializer;
import partition.MyPartitioner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Producer {
    public static void main(String[] args) throws Exception {
        // Producer Config
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.101.134:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 分区策略
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
        // Producer 压缩
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        System.out.println(properties);
        // Create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        String topic = "topic02";
        String key = "key02";
        String value = "value03" + LocalDateTime.now();
        // Create Message Record
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

        // 简单发送
        // producer.send(record);
        // producer.close();

        // 同步发送
        // try {
        //    RecordMetadata metadata = producer.send(record).get();
        //    System.out.println(metadata);
        //    System.out.println(metadata.topic());
        //    System.out.println(metadata.partition());
        //    System.out.println(metadata.offset());
        // } catch (Exception e) {
        //   throw new RuntimeException(e);
        // }
        // producer.close();

        // 异步发送
        // producer.send(record, new Callback() {
        //    @Override
        //    public void onCompletion(RecordMetadata metadata, Exception e) {
        //        if (e != null) {
        //           e.printStackTrace();
        //        }
        //        System.out.println(metadata.topic());
        //        System.out.println(metadata.partition());
        //        System.out.println(metadata.offset());
        //    }
        // });

        //
        for (int i = 0; i < 100; i++) {
            value = "value03" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            // Create Message Record
            record = new ProducerRecord<>(topic, key, value);
            producer.send(record);
            TimeUnit.MILLISECONDS.sleep(500L);
        }
        producer.close();

    }
}
