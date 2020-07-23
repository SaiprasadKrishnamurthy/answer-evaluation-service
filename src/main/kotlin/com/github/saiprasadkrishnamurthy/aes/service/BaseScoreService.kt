package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score

/**
 * @author Sai.
 */
abstract class BaseScoreService(private val messagePublisher: MessagePublisher) {
    abstract fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score

    fun computeScore(questionAnswerMetadata: QuestionAnswerMetadata) {
        messagePublisher.broadcastScore(getScore(questionAnswerMetadata))
    }
}