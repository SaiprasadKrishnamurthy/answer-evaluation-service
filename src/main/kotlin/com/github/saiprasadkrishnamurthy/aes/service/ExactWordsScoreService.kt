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
        return if (questionAnswerMetadata.actualAnswer.isNotBlank()) {
            // your logic call Score.n(...) with the actual score.
            val actualAnswerIgnoringSpaces = questionAnswerMetadata.actualAnswer.replace("\\s+", "")
            val exactAnswers = questionAnswerMetadata.exactTexts
            var score = 0.0;
            exactAnswers.forEach {
                val keywordIgnoringSpaces = it.keyword.replace("\\s+","")
                if (actualAnswerIgnoringSpaces.contains(keywordIgnoringSpaces, true)) {
                    score++;
                }
            }
            Score.n(qmId = questionAnswerMetadata.id, answerType = AnswerType.actual, type = "exactWords", n = score)
        } else {
            Score.one(qmId = questionAnswerMetadata.id, answerType = AnswerType.expected, type = "exactWords")
        }
    }
}