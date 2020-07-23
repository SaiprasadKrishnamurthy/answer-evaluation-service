package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.AnswerType
import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score
import com.textrazor.TextRazor
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service


/**
 * @author Sai.
 */
@Service
class TopicsSimilarityScoreService(messagePublisher: MessagePublisher, private val environment: Environment) : BaseScoreService(messagePublisher) {
    override fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score {
        val apiKey = environment.getProperty("apiKey")
        return when {
            questionAnswerMetadata.actualAnswer.isNotBlank() -> {
                val client = TextRazor(apiKey)
                client.addExtractor("words")
                client.addExtractor("entities")
                client.addExtractor("topics")
                val response = client.analyze(questionAnswerMetadata.actualAnswer)
                val topics = response.response.topics.map { it.label.toLowerCase() }
                val expectedTopics = questionAnswerMetadata.topics.map { it.keyword }
                val matches = expectedTopics.filter { topics.contains(it) }.size
                Score.n(qmId = questionAnswerMetadata.id, answerType = AnswerType.actual, type = "topics", n = matches.toDouble() * questionAnswerMetadata.weightages.getOrDefault("topics", 1.0))
            }
            questionAnswerMetadata.topics.isNotEmpty() -> Score.n(qmId = questionAnswerMetadata.id, answerType = AnswerType.expected, type = "topics", n = questionAnswerMetadata.topics.size * questionAnswerMetadata.weightages.getOrDefault("keywords", 1.0))
            else -> Score.zero(qmId = questionAnswerMetadata.id, answerType = AnswerType.expected, type = "topics")
        }
    }
}
