package com.github.saiprasadkrishnamurthy.aes.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score
import com.github.sonus21.rqueue.core.RqueueMessageTemplate
import com.github.sonus21.rqueue.producer.RqueueMessageSender
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Slf4j
@Service
class RedisMessagePublisher(private val redisTemplate: RedisTemplate<String, String>,
                            @Value("\${questionAnswerChangedQueue}") private val questionAnswerChangedQueue: String,
                            @Value("\${scoreQueue}") private val scoreQueue: String) : MessagePublisher {
    override fun broadcastScore(score: Score) {
        send(scoreQueue, OBJECT_MAPPER.writeValueAsString(score))
    }

    private fun send(queue: String, json: String) {
        val rqueueMessageTemplate = RqueueMessageTemplate(redisTemplate.connectionFactory)
        val rqueueMessageSender = RqueueMessageSender(rqueueMessageTemplate)
        rqueueMessageSender.put(queue, json)
    }

    override fun broadcastQuestionAnswerChanges(questionAnswerMetadata: QuestionAnswerMetadata) {
        send(questionAnswerChangedQueue, OBJECT_MAPPER.writeValueAsString(questionAnswerMetadata))
    }

    companion object {
        private val OBJECT_MAPPER = ObjectMapper()
    }
}
