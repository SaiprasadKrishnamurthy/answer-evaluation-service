package com.github.saiprasadkrishnamurthy.aes.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import lombok.Data
import org.apache.commons.codec.binary.Base64
import org.apache.http.HttpHost
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.security.KeyStore

/**
 * @author Sai.
 */
@Configuration
@EnableEncryptableProperties
@Data
class ElasticConfig {


    @Value("\${esUrl}")
    var esUrl: String? = null
        set(esUrl) {
            field = this.esUrl
        }

    @Value("\${esKeywordsIndex}")
    var esKeywordsIndex: String? = null
        set(esKeywordsIndex) {
            field = this.esKeywordsIndex
        }

    @Value("\${esUsername}")
    var esUsername: String? = null
        set(esUsername) {
            field = this.esUsername
        }

    @Value("\${esPassword}")
    var esPassword: String? = null
        set(esPassword) {
            field = this.esPassword
        }

    @Value("\${useProxy}")
    var isUseProxy: Boolean = false
        set(useProxy) {
            field = isUseProxy
        }

    @Value("\${esJksFile}")
    var esJksFile: String? = null
        set(esJksFile) {
            field = this.esJksFile
        }

    @Value("\${esJksPassword}")
    var esJksPassword: String? = null
        set(esJksPassword) {
            field = this.esJksPassword
        }

    @Bean("esRestTemplate")
    fun esRestTemplate(): RestTemplate {

        try {
            if (!this.esUrl!!.contains("https")) {
                val stringHttpMessageConverter = StringHttpMessageConverter()
                stringHttpMessageConverter.defaultCharset = Charset.defaultCharset()
                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(stringHttpMessageConverter)
                return restTemplate
            }
            var keystoreFile: InputStream? = null
            try {
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                if (File(this.esJksFile!!).exists()) {
                    keystoreFile = FileInputStream(this.esJksFile!!)
                } else {
                    keystoreFile = ElasticConfig::class.java.classLoader.getResourceAsStream(this.esJksFile!!)
                }
                keyStore.load(keystoreFile, this.esJksPassword!!.toCharArray())

                val socketFactory = SSLConnectionSocketFactory(
                        SSLContextBuilder()
                                .loadTrustMaterial(null, TrustSelfSignedStrategy())
                                .loadKeyMaterial(keyStore, null) // Can be null as the key is already loaded using the password.
                                .build(),
                        NoopHostnameVerifier.INSTANCE)

                val httpClientBuilder = HttpClients
                        .custom()
                if (isUseProxy) {
                    // ok - hardcoded!
                    httpClientBuilder.setProxy(HttpHost("192.168.46.100", 3128, "http"))
                }
                httpClientBuilder.setSSLSocketFactory(socketFactory).build()
                val requestFactory = HttpComponentsClientHttpRequestFactory(httpClientBuilder.build())
                return RestTemplate(requestFactory)
            } finally {
                if (keystoreFile != null) {
                    try {
                        keystoreFile.close()
                    } catch (ignore: IOException) {
                        // ignored.
                    }

                }
            }
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }

    }

    /**
     * Necessary HTTP headers for the Datalake.
     *
     * @return HttpHeaders.
     */
    @Qualifier("esAuthHeaders")
    @Bean
    fun esAuthHeaders(): MultiValueMap<String, String> {
        return object : HttpHeaders() {
            init {
                val auth = esUsername + ":" + esPassword
                val encodedAuth = Base64.encodeBase64(auth.toByteArray(Charset.defaultCharset()))
                val authHeader = "Basic " + String(encodedAuth)
                set("Authorization", authHeader)
                contentType = MediaType.APPLICATION_JSON
            }
        }
    }
}
