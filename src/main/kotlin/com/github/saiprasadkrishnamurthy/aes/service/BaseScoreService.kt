package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score
import com.github.saiprasadkrishnamurthy.aes.repository.ScoreRepository

/**
 * @author Sai.
 */
abstract class BaseScoreService(private val messagePublisher: MessagePublisher, private val scoreRepository: ScoreRepository) {
    internal abstract fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score

    fun computeScore(questionAnswerMetadata: QuestionAnswerMetadata) {
        val score = getScore(questionAnswerMetadata)
        println("${this::class.java}  --   $score")
        messagePublisher.broadcastScore(score)
    }

    fun computeScoreSync(questionAnswerMetadata: QuestionAnswerMetadata) {
        val score = getScore(questionAnswerMetadata)
        println("${this::class.java}  --   $score")
        scoreRepository.deleteByQuestionAnswerMetadataIdAndTypeAndAnswerType(score.questionAnswerMetadataId, score.type, score.answerType.toString())
        scoreRepository.save(score)
    }
}