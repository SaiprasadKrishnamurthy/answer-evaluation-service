package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.AnswerType
import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class SpellMistakesScoreService(messagePublisher: MessagePublisher) : BaseScoreService(messagePublisher) {
    override fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score {
        println(" Implement Me Kumar!")
        return if (questionAnswerMetadata.actualAnswer.isNotBlank()) {
            Score.zero(qmId = questionAnswerMetadata.id, answerType = AnswerType.expected, type = "spellMistakes")
        } else {
            Score.zero(qmId = questionAnswerMetadata.id, answerType = AnswerType.actual, type = "spellMistakes")
        }
    }
}