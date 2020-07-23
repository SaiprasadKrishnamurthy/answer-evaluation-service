package com.github.saiprasadkrishnamurthy.aes.repository

import com.github.saiprasadkrishnamurthy.aes.model.Score
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * @author Sai.
 */
@Repository
interface ScoreRepository : MongoRepository<Score, String> {
    fun deleteByQuestionAnswerMetadataIdAndType(questionId: String, type: String)
    fun findByQuestionAnswerMetadataIdAndType(questionId: String, type: String): List<Score>
}

