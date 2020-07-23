package com.github.saiprasadkrishnamurthy.aes.messaging

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.saiprasadkrishnamurthy.aes.model.KeywordsService
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataChangedListener
import com.github.sonus21.rqueue.annotation.RqueueListener
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class RedisQuestionAnswerMetadataChangedListener(val keywordsService: KeywordsService) : QuestionAnswerMetadataChangedListener {
    @RqueueListener(value = ["\${questionAnswerChangedQueue}"])
    override fun questionAnswerMetadataChanged(json: String) {
        keywordsService.registerKeywords(jacksonObjectMapper().readValue(json, QuestionAnswerMetadata::class.java))
    }
}