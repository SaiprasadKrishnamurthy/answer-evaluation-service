package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.AnswerType
import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score
import org.springframework.core.env.Environment
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * @author Sai.
 */
@Service
class SemanticSimilarityScoreService(messagePublisher: MessagePublisher, private val environment: Environment) : BaseScoreService(messagePublisher) {
    val restTemplate = RestTemplate()
    override fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score {
        val textSemanticSimilarityApi = environment.getProperty("textSemanticSimilarityApi")!!
        val apiHostHeader = environment.getProperty("apiHostHeader")
        val apiKey = environment.getProperty("apiKey")
        val httpHeaders = HttpHeaders()
        httpHeaders["x-rapidapi-host"] = apiHostHeader
        httpHeaders["x-rapidapi-key"] = apiKey
        return if (questionAnswerMetadata.actualAnswer.isNotBlank()) {
            val response = restTemplate.exchange(String.format(textSemanticSimilarityApi, questionAnswerMetadata.rawAnswer, questionAnswerMetadata.actualAnswer), HttpMethod.GET, HttpEntity<String>(httpHeaders), Map::class.java)
            val score = response.body?.getOrDefault("similarity", 0.0).toString()
            Score.n(qmId = questionAnswerMetadata.id, answerType = AnswerType.actual, type = "semantic", n = score.toDouble() * questionAnswerMetadata.weightages.getOrDefault("semantic", 1.0))
        } else {
            Score.n(qmId = questionAnswerMetadata.id, answerType = AnswerType.expected, type = "semantic", n = questionAnswerMetadata.weightages.getOrDefault("semantic", 1.0))
        }
    }
}