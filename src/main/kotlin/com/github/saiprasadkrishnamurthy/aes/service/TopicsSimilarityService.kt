package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.ExpectedAnswer
import com.textrazor.TextRazor
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
class TopicsSimilarityService(private val environment: Environment){

    fun getSimiliarTopics(expectedAnswer: ExpectedAnswer
    ): List<String>{
        val apiKey = environment.getProperty("textTopicsApiKey")
        val client = TextRazor(apiKey)
        client.addExtractor("words")
        client.addExtractor("entities")
        client.addExtractor("topics")
        val response = client.analyze(expectedAnswer.answer)
        return response.response.topics.take(20).map { it.label.toLowerCase() }
    }

}