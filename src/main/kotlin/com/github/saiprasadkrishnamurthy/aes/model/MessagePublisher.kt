package com.github.saiprasadkrishnamurthy.aes.model

interface MessagePublisher {
    fun broadcastQuestionAnswerChanges(questionAnswerMetadata: QuestionAnswerMetadata)
    fun broadcastScore(score: Score)
}
