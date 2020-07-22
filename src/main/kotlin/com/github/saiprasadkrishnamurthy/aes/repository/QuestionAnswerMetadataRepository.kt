package com.github.saiprasadkrishnamurthy.aes.repository

import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

/**
 * @author Sai.
 */
@Repository
interface QuestionAnswerMetadataRepository : MongoRepository<QuestionAnswerMetadata, String> {
}

