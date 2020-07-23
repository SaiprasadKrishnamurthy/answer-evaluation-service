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
                                  var createdDateTime: Long,
                                  var modifiedDateTime: Long,
                                  val keywords: List<Text>,
                                  val maxKeywordMarks: Int,
                                  val phrases: List<Text>,
                                  val topics: List<Text>,
                                  val exactTexts: List<Text>,
                                  val maxPhrasesMarks: Int,
                                  val penaltyRules: List<PenaltyRule>)

data class Text(val keyword: String, val marks: Int, val synonyms: List<String>)
data class PenaltyRule(val penalty: String, val marks: Int, val maxPenaltyMarks: Int)
data class KeywordMatchRequest(val questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier, val answer: String)
data class KeywordMatchResponse(val questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier, val score: Double = 0.0, val texts: List<String>)

data class Score(@Id val id: String, val questionId: String, val type: String, val score: Double = 0.0)

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