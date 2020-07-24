package com.github.saiprasadkrishnamurthy.aes.messaging

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.saiprasadkrishnamurthy.aes.model.Score
import com.github.saiprasadkrishnamurthy.aes.model.ScoreListener
import com.github.saiprasadkrishnamurthy.aes.repository.ScoreRepository
import com.github.sonus21.rqueue.annotation.RqueueListener
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class RedisScoreReceivedListener(val scoreRepository: ScoreRepository) : ScoreListener {
    @RqueueListener(value = ["\${scoreQueue}"])
    override fun scoreReceived(json: String) {
        try {
            println(json)
            println("\n\n")
            val score = jacksonObjectMapper().readValue(json, Score::class.java)
            scoreRepository.deleteByQuestionAnswerMetadataIdAndTypeAndAnswerType(score.questionAnswerMetadataId, score.type, score.answerType.toString())
            scoreRepository.save(score)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}