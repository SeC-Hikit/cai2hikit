package com.hikit.cai2hikit.conf

import com.hikit.cai2hikit.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CaiRestClientConfiguration(@Value("\${resource.path}") private val basePath : String) {
    @Bean
    fun getTrailRestClient(): RestClient {
        return RestClient(basePath)
    }
}
