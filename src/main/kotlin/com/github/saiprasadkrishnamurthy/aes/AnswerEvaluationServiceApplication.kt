package com.github.saiprasadkrishnamurthy.aes

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * @author Sai.
 */
@SpringBootApplication
@EnableCaching
@EnableEncryptableProperties
@EnableMongoRepositories
@EnableSwagger2
@EnableScheduling
class EventRelationshipsAnalysisServiceApplication

fun main(args: Array<String>) {
    runApplication<EventRelationshipsAnalysisServiceApplication>(*args)
}
