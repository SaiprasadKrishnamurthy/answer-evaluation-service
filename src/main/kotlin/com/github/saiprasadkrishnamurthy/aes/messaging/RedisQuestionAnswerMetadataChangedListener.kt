package com.github.saiprasadkrishnamurthy.aes.messaging

import com.github.saiprasadkrishnamurthy.aes.model.QuestionAnswerMetadataChangedListener
import com.github.sonus21.rqueue.annotation.RqueueListener
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class RedisQuestionAnswerMetadataChangedListener : QuestionAnswerMetadataChangedListener {
    @RqueueListener(value = ["\${questionAnswerChangedQueue}"])
    override fun questionAnswerMetadataChanged(json: String) {
        println(json)
    }
}