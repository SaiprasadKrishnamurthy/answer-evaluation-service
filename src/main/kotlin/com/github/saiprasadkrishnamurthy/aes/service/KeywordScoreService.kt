package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class KeywordScoreService(messagePublisher: MessagePublisher) : BaseScoreService(messagePublisher) {
    override fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}