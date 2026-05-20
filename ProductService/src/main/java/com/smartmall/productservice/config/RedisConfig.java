package com.smartmall.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmall.productservice.command.entity.Product;

@Configuration
public class RedisConfig {

    @Bean
    RedisConnectionFactory redisConnectionFactory() {

        return new LettuceConnectionFactory();
    }

    @Bean
    RedisTemplate<String, Product> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Product> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        // Key Serializer
        template.setKeySerializer(
                new StringRedisSerializer());

        // Jackson 3 ObjectMapper
//        ObjectMapper mapper = new ObjectMapper();

        // Value Serializer
        template.setValueSerializer(
                 GenericJacksonJsonRedisSerializer
                .builder()
                .build());

        template.afterPropertiesSet();

        return template;
    }
}