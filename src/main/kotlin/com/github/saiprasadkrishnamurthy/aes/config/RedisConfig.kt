package com.github.saiprasadkrishnamurthy.aes.config

import com.github.sonus21.rqueue.config.SimpleRqueueListenerContainerFactory
import com.github.sonus21.rqueue.spring.EnableRqueue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import redis.clients.jedis.JedisPoolConfig
import java.time.Duration


/**
 * @author Sai.
 */
@EnableRqueue
@Configuration
class RedisConfig {
    @Bean
    fun redisConnectionFactory(environment: Environment): RedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration()
        redisStandaloneConfiguration.hostName = environment.getProperty("redis.host")!!
        redisStandaloneConfiguration.port = environment.getProperty("redis.port")!!.toInt()
        redisStandaloneConfiguration.database = environment.getProperty("redis.database")!!.toInt()
        redisStandaloneConfiguration.password = RedisPassword.of(environment.getProperty("redis.password")!!)
        val jedisClientConfiguration = JedisClientConfiguration.builder()
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60))// 60s connection timeout
        jedisClientConfiguration.usePooling().poolConfig(buildPoolConfig())
        return JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build())
    }

    private fun buildPoolConfig(): JedisPoolConfig {
        // TODO from config.
        val poolConfig = JedisPoolConfig()
        poolConfig.maxTotal = 10
        poolConfig.maxIdle = 10
        poolConfig.testOnBorrow = false
        poolConfig.testOnReturn = false
        poolConfig.testWhileIdle = false
        poolConfig.minEvictableIdleTimeMillis = Duration.ofSeconds(60).toMillis()
        poolConfig.timeBetweenEvictionRunsMillis = Duration.ofSeconds(30).toMillis()
        poolConfig.blockWhenExhausted = false
        poolConfig.testOnCreate = true
        return poolConfig
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.setDefaultSerializer(StringRedisSerializer())
        template.setConnectionFactory(redisConnectionFactory)
        return template
    }

    @Bean
    fun simpleRqueueListenerContainerFactory(redisConnectionFactory: RedisConnectionFactory): SimpleRqueueListenerContainerFactory {
        val factory = SimpleRqueueListenerContainerFactory()
        factory.maxNumWorkers = 4
        factory.redisConnectionFactory = redisConnectionFactory
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.setThreadNamePrefix("taskExecutor")
        threadPoolTaskExecutor.corePoolSize = 4
        threadPoolTaskExecutor.maxPoolSize = 8
        threadPoolTaskExecutor.setQueueCapacity(100)
        threadPoolTaskExecutor.afterPropertiesSet()
        factory.taskExecutor = threadPoolTaskExecutor
        return factory
    }
}