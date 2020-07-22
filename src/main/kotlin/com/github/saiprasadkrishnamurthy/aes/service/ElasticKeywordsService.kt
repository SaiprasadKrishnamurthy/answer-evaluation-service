package com.github.saiprasadkrishnamurthy.aes.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.saiprasadkrishnamurthy.aes.config.ElasticConfig
import com.github.saiprasadkrishnamurthy.aes.model.KeywordsService
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class ElasticKeywordsService(private val elasticConfig: ElasticConfig) : KeywordsService {
    val keywordsIndexQueryFuzzyFragmentJson = """
        {
          "function_score": {
            "query": {
              "fuzzy": {
                "text": {
                  "value": "%s"
                }
              }
            },
            "boost_mode": "replace",
            "functions": [
              {
                "weight": %s
              }
            ]
          }
        }
    """.trimIndent()

    val keywordsIndexQueryExactFragmentJson = """
        {
          "function_score": {
            "query": {
              "match": {
                "text":  "%s"
                }
              }
            },
            "boost_mode": "replace",
            "functions": [
              {
                "weight": %s
              }
            ]
          }
    """.trimIndent()

    val keywordsIndexQueryTemplateJson = """
        {
  "query": {
    "bool": {
      "should": [
        %s
      ],
      "filter": [
        { "term":  { "quesionId": "%s" }},
        { "term":  { "clientId": "%s" }},
        { "term":  { "subjectId": "%s" }},
        { "term":  { "classId": "%s" }}
      ]
    }
  }
}
    """.trimIndent()


    override fun registerKeywords(questionAnswerMetadata: QuestionAnswerMetadata) {
        val fuzzy = buildTextQuery(questionAnswerMetadata, keywordsIndexQueryFuzzyFragmentJson)
        val fuzzySynonyms = buildTextQueryForSynonyms(questionAnswerMetadata, keywordsIndexQueryFuzzyFragmentJson)
        val regular = buildTextQuery(questionAnswerMetadata, keywordsIndexQueryExactFragmentJson)
        val regularSynonyms = buildTextQueryForSynonyms(questionAnswerMetadata, keywordsIndexQueryExactFragmentJson)
        val queryFragments = listOf(fuzzy, fuzzySynonyms, regular, regularSynonyms).flatten().joinToString(",")
        val q = String.format(keywordsIndexQueryTemplateJson,
                queryFragments,
                questionAnswerMetadata.identifier.questionId,
                questionAnswerMetadata.identifier.clientId,
                questionAnswerMetadata.identifier.subjectId,
                questionAnswerMetadata.identifier.classId)
        LOG.info("Query: {} ", q)
        val uri = "${elasticConfig.esUrl}/${elasticConfig.esKeywordsIndex}/_doc/${questionAnswerMetadata.id}"
        val response = elasticConfig.esRestTemplate().exchange(uri, HttpMethod.PUT, HttpEntity(OM.readValue(q, Map::class.java), elasticConfig.esAuthHeaders()), Map::class.java)
        LOG.info("Response: {}", response.body)
    }

    private fun buildTextQueryForSynonyms(questionAnswerMetadata: QuestionAnswerMetadata, queryFragment: String): List<String> {
        return questionAnswerMetadata.keywords
                .filter { it.synonyms.isNotEmpty() }
                .flatMap {
                    it.synonyms.map { s -> String.format(queryFragment, s, it.marks) }
                }
    }

    private fun buildTextQuery(questionAnswerMetadata: QuestionAnswerMetadata, queryFragment: String): List<String> {
        return questionAnswerMetadata.keywords.map {
            String.format(queryFragment, it.keyword, it.marks)
        }
    }

    companion object {
        val LOG = LoggerFactory.getLogger(ElasticKeywordsService::class.java)
        val OM = jacksonObjectMapper()
    }
}