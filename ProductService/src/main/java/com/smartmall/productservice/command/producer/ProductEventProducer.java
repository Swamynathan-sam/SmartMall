package com.smartmall.productservice.command.producer;

import com.smartmall.productservice.common.event.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

    private final KafkaTemplate<String,
            ProductCreatedEvent> kafkaTemplate;

    public void publish(ProductCreatedEvent event) {

        kafkaTemplate.send(
                "product-created-topic",
                event);
    }
}