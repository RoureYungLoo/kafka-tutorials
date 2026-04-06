package com.luruoyang.kafka01.controller;

//import jakarta.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/kafka/producer")
@Slf4j
public class ProducerController {

    @javax.annotation.Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/{msg}")
    public ResponseEntity<Boolean> sendMsgWithoutCallback(@PathVariable("msg") String msg) {
        kafkaTemplate.send("topic1", "1", msg);
        log.info("消息已发送：{}", msg);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/callback1/{msg}")
    public void sendMsg2(@PathVariable("msg") String msg) {
        kafkaTemplate.send("topic1", "666", msg).
                addCallback(result -> {
                    RecordMetadata metadata = result.getRecordMetadata();
                    String topic = metadata.topic();
                    int partition = metadata.partition();
                    long offset = metadata.offset();
                    log.warn("消息发送成功：topic: {}, partition: {}, offset: {}", topic, partition, offset);
                }, ex -> {
                    log.warn("消息发送失败：{}", ex.getMessage());
                });
    }

    @GetMapping("/callback2/{msg}")
    public void sendMsg3(@PathVariable("msg") String msg) {
        kafkaTemplate.send("topic1", "555", msg)
                .addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        log.warn("消息发送失败：{}", ex.getMessage());
                    }

                    @Override
                    public void onSuccess(@Nullable SendResult<String, Object> result) {
                        RecordMetadata metadata = result.getRecordMetadata();
                        log.warn("消息发送成功：topic: {}, partition: {}, offset: {}",
                                metadata.topic(), metadata.partition(), metadata.offset());
                    }
                });
    }

}
