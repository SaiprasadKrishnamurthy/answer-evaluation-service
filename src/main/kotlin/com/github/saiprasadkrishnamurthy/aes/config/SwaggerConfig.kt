package com.github.saiprasadkrishnamurthy.aes.config

import com.google.common.base.Predicates
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

/**
 * @author Sai.
 */
@Configuration
class SwaggerConfig(private val environment: Environment) {
    @Bean
    fun configApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .groupName("aes")
                //.host("https://github.com/SaiprasadKrishnamurthy")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error"))) // Exclude Spring error controllers
                .build()
    }

    @Bean
    fun pingElasticsearch(elasticConfig: ElasticConfig) = CommandLineRunner {
        val response = elasticConfig.esRestTemplate().exchange(elasticConfig.esUrl + "/_cat/indices", HttpMethod.GET, HttpEntity<String>(elasticConfig.esAuthHeaders()), String::class.java)
        println("Elasticsearch Returned: $response")
        println("   ** ELASTICSEARCH PING OK ** \n\n ")
    }

    @Bean
    fun pingMongo(mongoTemplate: MongoTemplate) = CommandLineRunner {
        val colls = mongoTemplate.db.listCollectionNames().map { it.toUpperCase() }.toList()
        println(colls.joinToString("\n"))
        println("   ** MONGO PING OK ** \n\n ")
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Answer Evaluation Service | REST APIs")
                .contact(Contact("The Uncomplicated", "www.sita.aero", "saiprasad.krishnamurthy@sita.aero,pankaj.jain2@sita.aero"))
                .version("Build: " + environment.getProperty("build.version")!!)
                .build()
    }
}