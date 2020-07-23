package com.github.saiprasadkrishnamurthy.aes.repository

import com.github.saiprasadkrishnamurthy.aes.model.TotalScore
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * @author Sai.
 */
@Repository
interface TotalScoreRepository : MongoRepository<TotalScore, String>

