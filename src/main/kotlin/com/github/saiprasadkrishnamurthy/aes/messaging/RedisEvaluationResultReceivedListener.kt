package com.github.saiprasadkrishnamurthy.aes.messaging

import com.github.saiprasadkrishnamurthy.aes.model.EvaluationResultListener
import com.github.sonus21.rqueue.annotation.RqueueListener
import org.springframework.stereotype.Service

/**
 * @author Sai.
 */
@Service
class RedisEvaluationResultReceivedListener : EvaluationResultListener {
    @RqueueListener(value = ["\${evaluationResultReceivedQueue}"])
    override fun evaluationResultReceived(json: String) {

    }
}