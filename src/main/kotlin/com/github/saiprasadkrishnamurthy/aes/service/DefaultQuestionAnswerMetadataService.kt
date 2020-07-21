package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataIdentifier
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataService
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class DefaultQuestionAnswerMetadataService : QuestionAnswerMetadataService {

    @Cacheable("questionAnswerMetadata")
    override fun findByIdentifier(questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveOrUpdate(questionAnswerMetadata: QuestionAnswerMetadata) {

    }
}