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
class ExactWordsScoreService(messagePublisher: MessagePublisher) : BaseScoreService(messagePublisher) {
    override fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score {
        println(" Implement Me Kumar!")
        return if (questionAnswerMetadata.actualAnswer.isNotBlank()) {
            // your logic call Score.n(...) with the actual score.
            Score.zero(qmId = questionAnswerMetadata.id, answerType = AnswerType.actual, type = "exactWords")
        } else {
            Score.one(qmId = questionAnswerMetadata.id, answerType = AnswerType.expected, type = "exactWords")
        }
    }
}