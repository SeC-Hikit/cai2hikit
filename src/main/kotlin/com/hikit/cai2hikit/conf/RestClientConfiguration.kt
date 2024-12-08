package com.hikit.cai2hikit.conf

import com.hikit.cai2hikit.TrailRestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestClientConfiguration(@Value("\${resource.path}") private val basePath : String) {
    @Bean
    fun getTrailRestClient(): TrailRestClient {
        return TrailRestClient(basePath)
    }
}
