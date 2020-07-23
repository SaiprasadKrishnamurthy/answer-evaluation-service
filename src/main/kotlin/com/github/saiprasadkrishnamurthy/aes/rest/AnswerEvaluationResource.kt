package com.github.saiprasadkrishnamurthy.aes.rest

import com.github.saiprasadkrishnamurthy.aes.model.Answer
import com.github.saiprasadkrishnamurthy.aes.model.TotalScore
import com.github.saiprasadkrishnamurthy.aes.repository.QuestionAnswerMetadataRepository
import com.github.saiprasadkrishnamurthy.aes.service.ScoringOrchestrationService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * @author Sai.
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin
class AnswerEvaluationResource(private val questionAnswerMetadataRepository: QuestionAnswerMetadataRepository,
                               private val scoreOrchestrationService: ScoringOrchestrationService) {
    @PostMapping("/answer", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun answer(@RequestBody answer: Answer): TotalScore =
            scoreOrchestrationService.overallScore(questionAnswerMetadataRepository.findById(answer.questionAnswerMetadataId).get().copy(studentId = answer.studentId, actualAnswer = answer.answer))
}