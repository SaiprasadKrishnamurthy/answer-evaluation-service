package com.github.saiprasadkrishnamurthy.aes.messaging

import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score

class TestMessagePublisher: MessagePublisher {
    override fun broadcastQuestionAnswerChanges(questionAnswerMetadata: QuestionAnswerMetadata) {
        TODO("Not yet implemented")
    }

    override fun broadcastScore(score: Score) {
        TODO("Not yet implemented")
    }
}