package com.github.saiprasadkrishnamurthy.aes.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * @author Sai.
 */
// TODO add more PJ
@Document
data class QuestionAnswerMetadata(@Id val id: String = UUID.randomUUID().toString(), val identifier: QuestionAnswerMetadataIdentifier)

data class QuestionAnswerMetadataIdentifier(val clientId: String, val subjectId: String, val questionId: String)

interface QuestionAnswerMetadataChangedListener {
    fun questionAnswerMetadataChanged(json: String)
}

interface EvaluationResultListener {
    fun evaluationResultReceived(json: String)
}

interface QuestionAnswerMetadataService {
    fun saveOrUpdate(questionAnswerMetadata: QuestionAnswerMetadata)
    fun findByIdentifier(questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier)
}