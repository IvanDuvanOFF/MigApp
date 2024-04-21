package org.example.migapi.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.example.migapi.core.config.redis.RedisProperties
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

@TestConfiguration
class TestRedisConfiguration(redisProperties: RedisProperties) {
    private val redisServer = RedisServer(redisProperties.redisPort)

    @PostConstruct
    fun postConstruct() {
        redisServer.start()
    }

    @PreDestroy
    fun preDestroy() {
        redisServer.stop()
    }
}