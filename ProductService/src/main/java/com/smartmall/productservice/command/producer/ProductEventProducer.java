package com.smartmall.productservice.command.producer;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

    private final KafkaTemplate<String,
            Object> kafkaTemplate;

    public void publish(Object event) {

        kafkaTemplate.send(
                "product-events",
                event);
    }
}