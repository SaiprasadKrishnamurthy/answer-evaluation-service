package com.github.saiprasadkrishnamurthy.aes.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.sonus21.rqueue.config.SimpleRqueueListenerContainerFactory
import io.lettuce.core.ClientOptions
import io.lettuce.core.TimeoutOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.time.Duration
import java.util.concurrent.TimeUnit
import org.springframework.data.redis.serializer.StringRedisSerializer



/**
 * @author Sai.
 */
@Configuration
class RedisConfig {
    @Bean
    fun jedisConnectionFactory(environment: Environment): RedisConnectionFactory {
        val redisConfiguration = RedisStandaloneConfiguration()
        redisConfiguration.database = environment.getProperty("redis.database", Int::class.java)!!
        redisConfiguration.setHostName(environment.getProperty("redis.host")!!)
        redisConfiguration.port = environment.getProperty("redis.port", Int::class.java)!!
        redisConfiguration.password = RedisPassword.of(environment.getProperty("redis.password", String::class.java))
        val clientOptions = ClientOptions.builder()
                .autoReconnect(true)
                .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.ofSeconds(10)).build())
                .requestQueueSize(Integer.MAX_VALUE)
                .build()
        val clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(10))
                .clientOptions(clientOptions)
                .build()
        return LettuceConnectionFactory(redisConfiguration, clientConfiguration)
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