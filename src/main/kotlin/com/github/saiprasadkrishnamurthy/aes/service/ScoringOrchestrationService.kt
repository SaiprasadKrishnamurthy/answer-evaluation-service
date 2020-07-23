package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class ScoringOrchestrationService(val applicationContext: ApplicationContext) {

    fun wordToVec(questionAnswerMetadata: QuestionAnswerMetadata) {
        val scoreServices = applicationContext.getBeansOfType(BaseScoreService::class.java)
        scoreServices.forEach { it.value.computeScore(questionAnswerMetadata) }
    }
}