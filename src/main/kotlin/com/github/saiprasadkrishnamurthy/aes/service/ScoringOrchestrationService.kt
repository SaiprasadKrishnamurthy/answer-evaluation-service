package com.github.saiprasadkrishnamurthy.aes.service

import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadata
import com.github.saiprasadkrishnamurthy.aes.model.TotalScore
import com.github.saiprasadkrishnamurthy.aes.repository.ScoreRepository
import com.github.saiprasadkrishnamurthy.aes.repository.TotalScoreRepository
import org.apache.commons.math3.ml.distance.EuclideanDistance
import org.apache.commons.math3.stat.StatUtils
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class ScoringOrchestrationService(val applicationContext: ApplicationContext,
                                  val scoreRepository: ScoreRepository,
                                  val totalScoreRepository: TotalScoreRepository) {

    fun wordToVec(questionAnswerMetadata: QuestionAnswerMetadata) {
        val scoreServices = applicationContext.getBeansOfType(BaseScoreService::class.java)
        scoreServices.values.parallelStream().forEach { it.computeScore(questionAnswerMetadata) }
    }

    fun overallScore(questionAnswerMetadata: QuestionAnswerMetadata): TotalScore {
        val expected = scoreRepository.findByQuestionAnswerMetadataIdAndType(questionAnswerMetadata.id, "expected").sortedBy { it.type }
        val actual = scoreRepository.findByQuestionAnswerMetadataIdAndType(questionAnswerMetadata.id, "actual").sortedBy { it.type }
        val v1 = expected.map { it.score }.toTypedArray()
        val v2 = actual.map { it.score }.toTypedArray()
        val x1 = DoubleArray(v1.size)
        val x2 = DoubleArray(v2.size)

        LOG.info("Expected vector: {}", v1.toList())
        LOG.info("Actual vector: {}", v2.toList())

        var z1 = DoubleArray(x1.size + x2.size)

        v1.forEachIndexed { i, v ->
            x1[i] = v
            z1[i] = v
        }
        v2.forEachIndexed { i, v ->
            x2[i] = v
            if (i > z1.size / 2) {
                z1[i] = v
            }
        }

        // Normalize.
        z1 = StatUtils.normalize(z1)
        z1.forEachIndexed { i, v ->
            if (i > z1.size / 2) {
                x2[i] = v
            } else {
                x1[i] = v
            }
        }

        LOG.info("Normalized Expected vector: {}", x1.toList())
        LOG.info("Normalized Actual vector: {}", x2.toList())

        val distance = EuclideanDistance().compute(x1, x2)
        LOG.info("Distance: {}", distance)
        val totalScore = (1 / (1 + distance)) * questionAnswerMetadata.totalMarks
        LOG.info("Total score: {}", totalScore)

        val ts = TotalScore(questionAnswerMetadata = questionAnswerMetadata, rawScores = actual, total = totalScore)
        totalScoreRepository.save(ts)
        return ts
    }

    companion object {
        val LOG = LoggerFactory.getLogger(ScoringOrchestrationService::class.java)
    }
}