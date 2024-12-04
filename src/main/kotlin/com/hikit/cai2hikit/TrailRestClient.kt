package com.hikit.cai2hikit

import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestClient

class TrailRestClient {
    private lateinit var restClient: RestClient

    fun init(baseUrl: String = "https://osm2cai.it/api/v2/"): TrailRestClient {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build()
        return this
    }

    fun getRestClient(): RestClient {
        return this.restClient
    }
}

@Bean
fun getTrailRestClient(urlIn: String) = TrailRestClient().init(urlIn)