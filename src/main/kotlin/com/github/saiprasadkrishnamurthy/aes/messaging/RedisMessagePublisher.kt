package com.github.saiprasadkrishnamurthy.aes.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Slf4j
@Service
class RedisMessagePublisher(private val redisTemplate: RedisTemplate<String, String>,
                            @Value("\${questionAnswerChangedQueue}") private val questionAnswerChangedQueue: String) : MessagePublisher {
    override fun broadcastQuestionAnswerChanges(questionAnswerMetadata: QuestionAnswerMetadata) {

    }


    companion object {
        private val OBJECT_MAPPER = ObjectMapper()
    }
}
