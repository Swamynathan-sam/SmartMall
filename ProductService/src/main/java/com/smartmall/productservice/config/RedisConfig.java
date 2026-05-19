package com.smartmall.productservice.config;

import tools.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    RedisConnectionFactory redisConnectionFactory() {

        return new LettuceConnectionFactory();
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Object> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        // Key Serializer
        template.setKeySerializer(
                new StringRedisSerializer());

        // Jackson 3 ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // Value Serializer
        template.setValueSerializer(
                new GenericJacksonJsonRedisSerializer(mapper));

        template.afterPropertiesSet();

        return template;
    }
}