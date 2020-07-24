package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.*
import com.github.saiprasadkrishnamurthy.aes.repository.ScoreRepository
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Sai.
 */
@Service
class KeywordScoreService(messagePublisher: MessagePublisher, val elasticKeywordsService: ElasticKeywordsService, scoreRepository: ScoreRepository) : BaseScoreService(messagePublisher, scoreRepository) {
    override fun getScore(questionAnswerMetadata: QuestionAnswerMetadata): Score {
        return if (questionAnswerMetadata.actualAnswer.isNotBlank()) {
            val keywordMatchRequest = KeywordMatchRequest(questionAnswerMetadataIdentifier = questionAnswerMetadata.identifier,
                    answer = questionAnswerMetadata.actualAnswer)
            val matchResponse = elasticKeywordsService.matchKeywords(keywordMatchRequest)
            Score(id = UUID.randomUUID().toString(),
                    questionAnswerMetadataId = questionAnswerMetadata.id,
                    answerType = AnswerType.actual,
                    score = matchResponse.score * questionAnswerMetadata.weightages.getOrDefault("keywords", 1.0),
                    explanation = matchResponse.texts,
                    type = "keywords")
        } else {
            elasticKeywordsService.registerKeywords(questionAnswerMetadata)
            Score(id = UUID.randomUUID().toString(),
                    questionAnswerMetadataId = questionAnswerMetadata.id,
                    answerType = AnswerType.expected,
                    score = questionAnswerMetadata.keywords.size.toDouble() * questionAnswerMetadata.weightages.getOrDefault("keywords", 1.0),
                    explanation = questionAnswerMetadata.keywords.map { it.keyword },
                    type = "keywords")
        }
    }
}