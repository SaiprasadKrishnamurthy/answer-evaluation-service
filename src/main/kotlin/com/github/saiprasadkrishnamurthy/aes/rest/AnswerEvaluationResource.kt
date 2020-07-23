package com.github.saiprasadkrishnamurthy.aes.rest

import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Sai.
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin
class AnswerEvaluationResource(private val questionAnswerMetadataService: QuestionAnswerMetadataService) {
}