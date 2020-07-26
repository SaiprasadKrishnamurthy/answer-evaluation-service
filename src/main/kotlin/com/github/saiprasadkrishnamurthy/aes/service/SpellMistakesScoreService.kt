package com.github.saiprasadkrishnamurthy.aes.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.saiprasadkrishnamurthy.aes.model.AnswerType
import com.github.saiprasadkrishnamurthy.aes.model.MessagePublisher
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.Score
import com.github.saiprasadkrishnamurthy.aes.repository.ScoreRepository
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class SpellMistakesScoreService(messagePublisher: MessagePublisher, private val environment: Environment, scoreRepository: ScoreRepository) : BaseScoreService(messagePublisher, scoreRepository) {

    val httpclient: HttpClient = HttpClients.createDefault()
    val spellCheckApi = environment.getProperty("spellCheckApi")!!
    val spellCheckApiKey = environment.getProperty("spellCheckApiKey")!!
    val spellCheckApiEnabled = environment.getProperty("spellCheckApiEnabled")!!

    override fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score {
        var score =0
         if (spellCheckApiEnabled.equals("true") && questionAnswerMetadata.actualAnswer.isNotBlank()) {

             val builder = URIBuilder(spellCheckApi)
             builder.setParameter("mode", "proof")
             builder.setParameter("mkt", "en-US")

             val uri = builder.build()
             val request = HttpPost(uri)
             request.setHeader("Content-Type", "application/x-www-form-urlencoded")
             request.setHeader("Ocp-Apim-Subscription-Key", spellCheckApiKey)

             // Request body
             val reqEntity = StringEntity("Text=${questionAnswerMetadata.actualAnswer}")
             request.entity = reqEntity
             val response = httpclient.execute(request)
             val entity = response.entity

             if (entity != null) {
                 val spellErrorsMap: MutableMap<*, *>? = ObjectMapper().readValue(EntityUtils.toString(entity), MutableMap::class.java)
                 println("spellErrorsMap: $spellErrorsMap")
                 val flaggeedTokens = spellErrorsMap!!["flaggedTokens"] as List<Map<String, Any>>?
                 score = flaggeedTokens!!.size
                 println("flaggeedTokens -----: $flaggeedTokens")
                 println("score -----: $score")
             }
            return Score.n(qmId = questionAnswerMetadata.id, answerType = AnswerType.actual, type = "spellMistakes", n = score.toDouble() * questionAnswerMetadata.weightages.getOrDefault("spellMistakes", 1.0))
         } else {
             return Score.n(qmId = questionAnswerMetadata.id, answerType = AnswerType.expected, type = "spellMistakes", n = questionAnswerMetadata.weightages.getOrDefault("spellMistakes", 1.0))
         }
    }

}