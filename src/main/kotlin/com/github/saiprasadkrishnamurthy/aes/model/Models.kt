package com.github.saiprasadkrishnamurthy.aes.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * @author Sai.
 */

data class QuestionAnswerMetadataIdentifier(val clientId: String, val subjectId: String, val questionId: String, val classId: String)

@Document
data class QuestionAnswerMetadata(@Id val id: String = UUID.randomUUID().toString(), val identifier: QuestionAnswerMetadataIdentifier,
                                  val rawQuestion: String,
                                  val rawAnswer: String,
                                  val author: String,
                                  val totalMarks: Int,
                                  val maxMarks: Int,
                                  var createdDateTime: Long = System.currentTimeMillis(),
                                  var modifiedDateTime: Long = System.currentTimeMillis(),
                                  val keywords: List<Text> = listOf(),
                                  val phrases: List<Text> = listOf(),
                                  val topics: List<Text> = listOf(),
                                  val exactTexts: List<Text> = listOf(),
                                  val weightages: Map<String, Double> = mapOf(),
                                  val actualAnswer: String = "",
                                  val studentId: String = "")

data class Text(val keyword: String, val marks: Int = 1, val synonyms: List<String>)
data class KeywordMatchRequest(val questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier, val answer: String)
data class KeywordMatchResponse(val questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier, val score: Double = 0.0, val texts: List<String>)

data class Answer(val studentId: String, val questionAnswerMetadataId: String, val answer: String)
data class ExpectedAnswer(val answer: String)

@Document
data class TotalScore(@Id val id: String = UUID.randomUUID().toString(),
                      val questionAnswerMetadata: QuestionAnswerMetadata,
                      val rawScores: List<Score>,
                      val total: Double)

enum class AnswerType {
    expected, actual
}

@Document
data class Score(@Id val id: String,
                 val questionAnswerMetadataId: String,
                 val type: String,
                 val score: Double = 0.0,
                 val answerType: AnswerType,
                 val explanation: List<String> = listOf()) {
    companion object {
        fun zero(qmId: String, answerType: AnswerType, type: String) =
                Score(id = UUID.randomUUID().toString(),
                        questionAnswerMetadataId = qmId,
                        answerType = answerType,
                        score = 0.0,
                        explanation = listOf(),
                        type = type)

        fun one(qmId: String, answerType: AnswerType, type: String) =
                Score(id = UUID.randomUUID().toString(),
                        questionAnswerMetadataId = qmId,
                        answerType = answerType,
                        score = 1.0,
                        explanation = listOf(),
                        type = type)

        fun n(qmId: String, answerType: AnswerType, type: String, n: Double) =
                Score(id = UUID.randomUUID().toString(),
                        questionAnswerMetadataId = qmId,
                        answerType = answerType,
                        score = n,
                        explanation = listOf(),
                        type = type)
        fun n(qmId: String, answerType: AnswerType, type: String, n: Double , explanation: List<String>) =
                Score(id = UUID.randomUUID().toString(),
                        questionAnswerMetadataId = qmId,
                        answerType = answerType,
                        score = n,
                        explanation = explanation,
                        type = type)
    }

}

interface QuestionAnswerMetadataChangedListener {
    fun questionAnswerMetadataChanged(json: String)
}

interface EvaluationResultListener {
    fun evaluationResultReceived(json: String)
}

interface ScoreListener {
    fun scoreReceived(json: String)
}

interface KeywordsService {
    fun registerKeywords(questionAnswerMetadata: QuestionAnswerMetadata)
    fun matchKeywords(keywordMatchRequest: KeywordMatchRequest): KeywordMatchResponse
}

interface QuestionAnswerMetadataService {
    fun saveOrUpdate(questionAnswerMetadata: QuestionAnswerMetadata)
    fun findByIdentifier(questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier): QuestionAnswerMetadata
    fun findById(id: String): QuestionAnswerMetadata
    fun findAll(): List<QuestionAnswerMetadata>
}