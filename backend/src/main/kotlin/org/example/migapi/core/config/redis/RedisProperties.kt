package org.example.migapi.core.config.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * Конфиг для redis
 */
@Configuration
class RedisProperties(
    @Value("\${spring.data.redis.port}")
    val redisPort: Int
)