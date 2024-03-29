package org.example.migapi.core.config.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class RedisProperties(
    @Value("\${spring.data.redis.port}")
    val redisPort: Int
)