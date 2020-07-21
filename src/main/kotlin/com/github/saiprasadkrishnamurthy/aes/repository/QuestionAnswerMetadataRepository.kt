package com.github.saiprasadkrishnamurthy.aes.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * @author Sai.
 */
@Repository
interface QuestionAnswerMetadataRepository : MongoRepository<QuestionAnswerMetadataRepository, String> {
}