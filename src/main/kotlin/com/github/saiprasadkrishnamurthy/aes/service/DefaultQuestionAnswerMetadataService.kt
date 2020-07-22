package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataIdentifier
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataService
import com.github.saiprasadkrishnamurthy.aes.repository.QuestionAnswerMetadataRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class DefaultQuestionAnswerMetadataService(private val questionAnswerMetadataRepository: QuestionAnswerMetadataRepository) : QuestionAnswerMetadataService  {
    override fun saveOrUpdate(questionAnswerMetadata: QuestionAnswerMetadata) {
        if(questionAnswerMetadata.id==null) {
            questionAnswerMetadata.createdDateTime = System.currentTimeMillis()
        }
        questionAnswerMetadata.modifiedDateTime= System.currentTimeMillis()
        questionAnswerMetadataRepository.save(questionAnswerMetadata)
    }

    override fun findByIdentifier(questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier): QuestionAnswerMetadata {
        TODO("Not yet implemented")
    }

    @Cacheable("questionAnswerMetadata")
    override fun findById(id: String): QuestionAnswerMetadata {
       return  questionAnswerMetadataRepository.findById(id).get()
    }

    override fun findAll(): List<QuestionAnswerMetadata> {
        return questionAnswerMetadataRepository.findAll()
    }
}