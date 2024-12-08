package com.hikit.cai2hikit

import org.springframework.web.client.RestClient

class RestClient(baseUrl: String) {
    private var restClient: RestClient =
        RestClient.builder().baseUrl(baseUrl).build()

    fun getClient(): RestClient {
        return restClient
    }
}

