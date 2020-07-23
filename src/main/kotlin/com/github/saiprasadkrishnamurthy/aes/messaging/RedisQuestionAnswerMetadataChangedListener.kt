package com.github.saiprasadkrishnamurthy.aes.messaging

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataChangedListener
import com.github.saiprasadkrishnamurthy.aes.service.ScoringOrchestrationService
import com.github.sonus21.rqueue.annotation.RqueueListener
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class RedisQuestionAnswerMetadataChangedListener(val scoringOrchestrationService: ScoringOrchestrationService) : QuestionAnswerMetadataChangedListener {
    @RqueueListener(value = ["\${questionAnswerChangedQueue}"])
    override fun questionAnswerMetadataChanged(json: String) {
        val qm = jacksonObjectMapper().readValue(json, QuestionAnswerMetadata::class.java)
        scoringOrchestrationService.wordToVec(qm)
    }
}