package com.luruoyang.kafka01.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/kafka/producer")
@Slf4j
public class ProducerController {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/{msg}")
    public ResponseEntity<Boolean> sendMsgWithoutCallback(@PathVariable("msg") String msg) {
        kafkaTemplate.send("topic1", "1", msg);
        log.info("消息已发送：{}", msg);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/callback1/{msg}")
    public void sendMsg2(@PathVariable("msg") String msg) {
    }

    @GetMapping("/callback2/{msg}")
    public void sendMsg3(@PathVariable("msg") String msg) {
    }

}
