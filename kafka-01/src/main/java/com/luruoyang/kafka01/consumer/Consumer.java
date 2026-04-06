package com.luruoyang.kafka01.consumer;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @KafkaListener(topics = {"topic1"})
    public void onMessage1(String message) {
//    public void onMessage1(ConsumerRecord<String, Object> record) {
//        log.warn("消费者收到消息：topic:{}, partition: {}, value: {}",
//                record.topic(), record.partition(), record.value());
        log.warn("消费者接收到消息：{}", message);

    }

}
