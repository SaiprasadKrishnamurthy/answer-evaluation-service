package com.github.saiprasadkrishnamurthy.aes.rest

import com.github.saiprasadkrishnamurthy.aes.model.ExpectedAnswer
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataIdentifier
import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataService
import com.github.saiprasadkrishnamurthy.aes.service.TopicsSimilarityService
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType

/**
 * @author Sai.
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin
class QuestionAnswerMetadataResource(private val questionAnswerMetadataService: QuestionAnswerMetadataService,
                                     private val topicsSimilarityService: TopicsSimilarityService) {

    @GetMapping("/question-answer-metadata/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@PathVariable("id") id: String) =
            questionAnswerMetadataService.findById(id)

    @PostMapping("/question-answer-metadata" , consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody questionAnswerMetadata: QuestionAnswerMetadata)=
            questionAnswerMetadataService.saveOrUpdate(questionAnswerMetadata)

    @PostMapping("/find-question-answer-metadata" , consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByIdentifier(@RequestBody questionAnswerMetadataIdentifier: QuestionAnswerMetadataIdentifier)=
            questionAnswerMetadataService.findByIdentifier(questionAnswerMetadataIdentifier)

    @GetMapping("/question-answer-metadata", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAll() = questionAnswerMetadataService.findAll()

    @PostMapping("/get-similiar-topics", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSimiliarTopicsForAnswer(@RequestBody expectedAnswer: ExpectedAnswer)=
            topicsSimilarityService.getSimiliarTopics(expectedAnswer)

}